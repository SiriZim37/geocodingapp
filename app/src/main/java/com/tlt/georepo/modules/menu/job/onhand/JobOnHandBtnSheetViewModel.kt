package com.tlt.georepo.modules.menu.job.onhand


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Color
import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import com.tlt.georepo.manager.JsonMapperManager
import com.tlt.georepo.manager.LocationManager
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.manager.geomap.GeoDataParser
import com.tlt.georepo.model.request.GetDataDirectionRequest
import com.tlt.georepo.model.request.JobOnHandRequest
import com.tlt.georepo.model.request.SetDataLocationRequest
import com.tlt.georepo.model.response.ItemJobOnhandResponse
import com.tlt.georepo.modules.menu.job.onhand.common.OnHandMainItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.ln


class JobOnHandBtnSheetViewModel : ViewModel() {
    val whenOvertime = MutableLiveData<Boolean>()
    val  whenDataLoadedLocationSuccess =MutableLiveData<LatLng>()
    val whenDataLoadedNoData = MutableLiveData<Boolean>()
    val whenLoading = MutableLiveData<Boolean>()
    val whenDataLoadedSuccess = MutableLiveData<Model>()
    val whenDataLoadedFailure = MutableLiveData<String>()
    private var job_MainOnhand : List<OnHandMainItem>? = null
    private val apiManager by lazy { GeoApiManager.getInstance() }
    val whenLoadDataCurrentLocation_arrive  = MutableLiveData<LatLng>()
    val whenLoadDataCurrentLocation_hold  = MutableLiveData<LatLng>()
    val whenLoadDataCurrentLocation_sendjob  = MutableLiveData<LatLng>()
    val whenLoadDataCurrentLocation_cancel  = MutableLiveData<LatLng>()
    val whenDataLoadedDirection  = MutableLiveData<String>()
    var isOvertime: Boolean = false

    var CurrentLocation : LatLng? = null

    fun getData(lat : String , lng : String) {
        GlobalScope.launch(Dispatchers.Main) {
            whenLoading.postValue(true)

            try {
                val model = getJobOnHand(lat , lng)

            } catch (e: Exception) {
                if (e.message != "device logon") {
                    whenDataLoadedFailure.postValue(e.message)
                }
                e.printStackTrace()
            }
        }
    }


    fun getDirection(lat : String , lng : String , desLat : String , desLng : String ) {
        GlobalScope.launch(Dispatchers.Main) {
            whenLoading.postValue(true)

            try {
                val model = getDataDirectionApi(lat , lng , desLat , desLng)

            } catch (e: Exception) {
                if (e.message != "device logon") {
                    whenDataLoadedFailure.postValue(e.message)
                }
                e.printStackTrace()
            }
        }
    }



    //****************  Call API ******************** //
         private suspend fun getJobOnHand(lat : String , lng: String) = suspendCoroutine<ItemJobOnhandResponse?> {
         var request = JobOnHandRequest.build(lat,lng)
         apiManager.getJobOnHand(request) { isError: Boolean, result: String ->
                 try{
                     whenLoading.value = false
                     if (isError) {
                         Log.e("JobOnHandRequest " , result)
//                         isOvertime = DatabaseManager.getInstance().getOvertime().isOverTime
                         if (result.equals("overtime")) {
                             whenOvertime.postValue(true)
                         }else{
                             it.resumeWithException(Exception(result))
                         }
                         return@getJobOnHand
                     }
                     try {

                         val items = JsonMapperManager.getInstance()
                             .gson.fromJson(result, Array<ItemJobOnhandResponse>::class.java)

                         if (items.isEmpty()) {
                             it.resumeWithException(Exception())
                             whenDataLoadedNoData.postValue(true)
                             return@getJobOnHand
                         }
                         val itemJobMainOnHand = items.map {
                             OnHandMainItem(
                                 aCTIONCODE = it.aCTIONCODE?:  "" ,
                                 aCTIONREMARK= it.aCTIONREMARK?:  "" ,
                                 aDDRESSID= it.aDDRESSID?:  "" ,
                                 bRANCH= it.bRANCH?:  "" ,
                                 cONTRACTNO= it.cONTRACTNO?:  "" ,
                                 cUSTOMERNAME= it.cUSTOMERNAME?:  "" ,
                                 iDCARD= it.iDCARD?:  "" ,
                                 iNSTALLMENTDUEAMT= it.iNSTALLMENTDUEAMT?:  "" ,
                                 jOBID= it.jOBID?:  "" ,
                                 lATITUDE= it.lATITUDE?:  "" ,
                                 lONGITUDE= it.lONGITUDE?:  "" ,
                                 mAINID= it.mAINID?:  "" ,
                                 pROFILEID= it.pROFILEID?:  "" ,
                                 rEALADDRESS= it.rEALADDRESS?:  "" ,
                                 rEGISTERNO= it.rEGISTERNO?:  "" ,
                                 rEGISTERPROVINCE= it.rEGISTERPROVINCE?:  ""
                             )
                         }.toMutableList()
                         job_MainOnhand = itemJobMainOnHand

                         val mainmodel = Model(
                             jobMainOnHandItem = job_MainOnhand?: listOf()
                         )
                         whenDataLoadedSuccess.postValue(mainmodel)
                         whenLoading.postValue(false)
                     }catch ( e : Exception){
                         Log.d("Error Connect API : " , e.stackTrace.toString())
                         whenLoading.postValue(false)
                     }
                 }catch ( e : Exception ) {
                     Log.e( "getJobOnHand : " , e.stackTrace.toString() )
                     whenLoading.postValue(false)
                 }
             }

         }

         data class Model(
             var jobMainOnHandItem: List<OnHandMainItem> = listOf()
         )


    fun GetCurrentLocationByProcess(typeMenu:String ) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val location = LocationManager.getLastKnowLocation()
                val latLng = LatLng(location.latitude, location.longitude)
                CurrentLocation = latLng
            if (typeMenu == "arrive")
            whenLoadDataCurrentLocation_arrive.postValue(latLng)
           else if (typeMenu == "cancel")
            whenLoadDataCurrentLocation_cancel.postValue(latLng)
              else if(typeMenu == "hold")
            whenLoadDataCurrentLocation_hold.postValue(latLng)
                 else if (typeMenu == "sendjob")
            whenLoadDataCurrentLocation_sendjob.postValue(latLng)



            } catch (error: Exception) {
                error.printStackTrace()
            }
        }
    }

    private fun getDataDirectionApi(lat : String , lng: String , desLat: String , desLng: String ){
       try{
        var request = GetDataDirectionRequest.build(
            lat = lat,
            lng = lng ,
            desLat = desLat ,
            desLng = desLng )
            apiManager.getDataDirection(request) { isError: Boolean, result: String ->
                if (isError) {
                    return@getDataDirection
                }
                val res = result
                whenDataLoadedDirection.postValue(result)
                return@getDataDirection
            }
        }catch (e : Exception) {
            e.message
        }


    }

    fun GetCurrentLocation() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val location = LocationManager.getLastKnowLocation()
                val latLng = LatLng(location.latitude, location.longitude)
                whenDataLoadedLocationSuccess.postValue(latLng)
            } catch (error: Exception) {
                error.printStackTrace()
            }
        }
    }

    fun SetCurrentLocation(lat: String , lng: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val request = SetDataLocationRequest.build(
                    lat = lat,
                    lng = lng
                )
                GeoApiManager.getInstance().setDataLocation(request) { isError, result ->

                    if (isError) {
                        return@setDataLocation
                    }
                }
            } catch (error: Exception) {
                error.printStackTrace()
            }
        }
    }
}


