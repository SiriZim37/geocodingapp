package com.tlt.georepo.modules.location.main

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.tlt.georepo.common.base.BaseViewModel
import com.tlt.georepo.manager.JsonMapperManager
import com.tlt.georepo.manager.LocationManager
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.manager.db.MasterDataManager
import com.tlt.georepo.model.entity.location.LocationDetailEntity
import com.tlt.georepo.util.PermissionUtils
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import com.tlt.georepo.common.exception.ApiException
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.model.request.ActionJobRequest
import com.tlt.georepo.model.request.JobCollectionRequest
import com.tlt.georepo.model.response.ItemJobCollectionResponse

class JobCollectionLocationViewModel : BaseViewModel() {
    val whenDataLoadedLocationSuccess = MutableLiveData<LatLng>()
    val whenDataLoadedNoData = MutableLiveData<Boolean>()
    val whenDataLoaded = MutableLiveData<Model>()
    val whenDataLoadedJobDataAll = MutableLiveData<JobModel>()
    val whenDataLoadedSuccess = MutableLiveData<JobModel>()
    val whenSendToApiFailure = MutableLiveData<String>()
    val whenSendToApiSuccess = MutableLiveData<String>()
    val whenOvertime = MutableLiveData<Boolean>()
    var isOvertime: Boolean = false


    private val NEARBY_LIMIT_ITEM = 20
    private val apiManager by lazy { GeoApiManager.getInstance() }
    private var job_list: List<JobCollectionMainItem>? = null

    fun getDataCollection(lat: String, lng: String) {
        whenLoading.postValue(true)

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val dataS = getJobCollection(lat, lng) // getPullJobCollection()
                whenDataLoadedSuccess.postValue(JobModel())
            } catch (e: ApiException) {
                whenDataFailure.postValue(e.message)
            } finally {
                whenLoading.postValue(false)
            }
        }
    }

    fun getData(selectedType: Type = Type.CUSTHOUSE) {

    }

    private fun getNearbyLocations() {
        GlobalScope.launch(Dispatchers.Main) {
            //            val locationList = getCustHouseLocationsByNearly()?.take(NEARBY_LIMIT_ITEM)
            val locationList = getCustHouseLocationsByNearly()
            // -- Not use because Service send Distance , It will open when not have data here
            whenDataLoadedJobDataAll.postValue(
                JobModel(
                    jobLocationList = locationList ?: listOf(),
                    jobLocationType = Type.CUSTHOUSE
                )
            )
        }
    }

    private suspend fun getCustHouseLocationsByNearly(): List<JobCollectionMainItem>? {
        if (!PermissionUtils.isGrantedLocation()) {
            return emptyList()
        }

        val currentLocation = LocationManager.getLastKnowLocation()

        return getCustHouseLocationsWithDistance(currentLocation, job_list)
//        getCustHouseLocationsWithDistance(currentLocation, job_list)
//            ?.sortedBy {
//                it.distance!!.toDouble()
//            }

    }

    private fun getCustHouseLocationsWithDistance(
        currentLocation: android.location.Location,
        JobList: List<JobCollectionMainItem>?
    ): List<JobCollectionMainItem>? {
        return JobList?.map {
            val destLocation = android.location.Location("dest").apply {
                latitude = it.lATITUDE?.toDouble() ?: 0.0
                longitude = it.lONGITUDE?.toDouble() ?: 0.0
            }
            var distance_km: String = ""
            if (it.lATITUDE != "0.0" && it.lONGITUDE != "0.0") {
//                val kilometers = currentLocation.distanceTo(destLocation).div(1000)
                distance_km = String.format("%.1f", currentLocation.distanceTo(destLocation).div(1000))
            }
            JobCollectionMainItem(
                aDDRESSID = it.aDDRESSID ?: "",
                bRANCH = it.bRANCH ?: "",
                cONTRACTNO = it.cONTRACTNO,
                cUSTOMERNAME = it.cUSTOMERNAME,
                aCTIONDATE = it.aCTIONDATE,
                iDCARD = it.iDCARD,
                iNSTALLMENTDUEAMT = it.iNSTALLMENTDUEAMT ?: "",
                jOBID = it.jOBID ?: "",
                lATITUDE = it.lATITUDE ?: "",
                lONGITUDE = it.lONGITUDE ?: "",
                mAINID = it.mAINID ?: "",
                pROFILEID = it.pROFILEID ?: "",
                rEALADDRESS = it.rEALADDRESS,
                rEGISTERNO = it.rEGISTERNO ?: "",
                rEGISTERPROVINCE = it.rEGISTERPROVINCE ?: "",
                aCTIONREMARK = it.aCTIONREMARK ?: "",
                aCTIONCODE = it.aCTIONCODE ?: "",
                rECORD_TYPE = it.rECORD_TYPE ?: "",
                oVERDUE_ITEM = it.oVERDUE_ITEM ?: "",
                dISTANCT_KM = it.dISTANCT_KM ?: "",
                distance = distance_km,
                type = Type.CUSTHOUSE
            )
        }
    }

    fun onLocationClick(index: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            val model = whenDataLoadedJobDataAll.value?.copy()

            val locations = model?.jobLocationList?.toMutableList()

            val firstItem = locations?.getOrNull(0)?.copy()
            val selectedItem = locations?.getOrNull(index)?.copy()

            locations?.set(0, selectedItem ?: JobCollectionMainItem())
            locations?.set(index, firstItem ?: JobCollectionMainItem())

            model?.jobLocationList = locations?.toList() ?: listOf()

            whenDataLoadedJobDataAll.postValue(model)
        }
    }

    fun saveLocationDetail(item: JobCollectionMainItem) {
        val locationDetail = LocationDetailEntity(
            branch = item.bRANCH ?: "",
            contractno = item.cONTRACTNO,
            customername = item.cUSTOMERNAME,
            idcard = item.iDCARD,
            installmentdueamt = item.iNSTALLMENTDUEAMT ?: "",
            jobid = item.jOBID ?: "",
            lattitude = item.lATITUDE ?: "",
            longtitude = item.lONGITUDE ?: "",
            mainid = item.mAINID ?: "",
            profileid = item.pROFILEID ?: "",
            realaddress = item.rEALADDRESS,
            registerno = item.rEGISTERNO ?: "",
            regisprovince = item.rEGISTERPROVINCE ?: "",
            recordtype = item.rECORD_TYPE ?: "",
            overdueitem = item.oVERDUE_ITEM ?: "",
            distance = item.dISTANCT_KM
        )

        LocationManager.saveLocationDetail(locationDetail)
    }

    data class Model(
        var locationList: List<CollectLocation> = listOf(),
        val locationType: Type = Type.CUSTHOUSE
    )

    data class CollectLocation(
        val code: String = "",
        val title: String = "",
        val address: String = "",
        val detail: String = "",
        val branch: String = "",
        val custhouseType: String = "",
        val openTime: String = "",
        val website: String = "",
        val responsibility: String = "",
        val tel: String = "",
        val telForCall: String = "",
        val distance: String = "",
        val lat: String = "",
        val lng: String = "",
        val type: Type = Type.CUSTHOUSE
    )

    enum class Type {
        CUSTHOUSE,
        NEARBY
    }

    data class JobModel(
        var jobLocationList: List<JobCollectionMainItem> = listOf(),
        val jobLocationType: Type = Type.CUSTHOUSE
    )

    data class JobCollectionMainItem(

        var aCTIONCODE: String = "",
        var aCTIONREMARK: String = "",
        var aCTIONDATE: String = "",
        var cONTRACTNO: String = "",
        var aDDRESSID: String = "",
        var bRANCH: String = "",
        var cUSTOMERNAME: String = "",
        var iDCARD: String = "",
        var iNSTALLMENTDUEAMT: String = "",
        var jOBID: String = "",
        var lATITUDE: String = "",
        var lONGITUDE: String = "",
        var mAINID: String = "",
        var pROFILEID: String = "",
        var rEALADDRESS: String = "",
        var rEGISTERNO: String = "",
        var rEGISTERPROVINCE: String = "",
        var rECORD_TYPE: String = "",
        var oVERDUE_ITEM: String = "",
        var distance: String = "",
        var dISTANCT_KM: String = "",
        var type: Type = Type.CUSTHOUSE

    )

    /*    -------- CALL SERVICE API --------  */

    private suspend fun getJobCollection(lat: String, lng: String) = suspendCoroutine<ItemJobCollectionResponse?> {
        var request = JobCollectionRequest.build(lat, lng)
        apiManager.getJobCollection(request) { isError: Boolean, result: String ->
            try {
                if (isError) {
//                    isOvertime = DatabaseManager.getInstance().getOvertime().isOverTime
                    if (result.equals("overtime")) {
                        whenOvertime.postValue(true)
                    }else {
                        it.resumeWithException(Exception(result))

                    }
                    return@getJobCollection
                }
                try {
                    val items = JsonMapperManager.getInstance()
                        .gson.fromJson(result, Array<ItemJobCollectionResponse>::class.java)
                        .toList()
                        .map {
                            JobCollectionMainItem(
                                aCTIONCODE =  it.aCTIONCODE
                                , aCTIONREMARK = it.aCTIONREMARK
                                , aCTIONDATE = it.aCTIONDATE
                                , aDDRESSID = it.aDDRESSID
                                , bRANCH = it.bRANCH
                                , cONTRACTNO = it.cONTRACTNO
                                , cUSTOMERNAME = it.cUSTOMERNAME
                                , iDCARD = it.iDCARD
                                , iNSTALLMENTDUEAMT = it.iNSTALLMENTDUEAMT
                                , jOBID = it.jOBID
                                , lATITUDE = it.lATITUDE
                                , lONGITUDE = it.lONGITUDE
                                , mAINID = it.mAINID
                                , pROFILEID = it.pROFILEID
                                , rEALADDRESS = it.rEALADDRESS
                                , rEGISTERNO = it.rEGISTERNO
                                , rEGISTERPROVINCE = it.rEGISTERPROVINCE
                                , rECORD_TYPE = it.rECORD_TYPE
                                , oVERDUE_ITEM = it.oVERDUE_ITEM
                                , dISTANCT_KM = it.dISTANCT_KM
                            )
                        }.sortedBy {
                            it.dISTANCT_KM!!.toDouble()
                        }

                    job_list = items
                    if (items.size == 0) {
                        whenDataLoadedNoData.postValue(true)
                    } else {
                        getNearbyLocations()
                    }
                } catch (e: Exception) {
                    Log.d("Error Connect API : ", e.stackTrace.toString())
                }
                return@getJobCollection
            } catch (e: Exception) {
                Log.e("getPullJobCollection : ", e.stackTrace.toString())
            }
        }
    }


    /*    -------- CALL SERVICE API --------  */

    fun sendActionJob(
        type: String,
        main_id: String,
        lat: String,
        lng: String
    ) {

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val location = LocationManager.getLastKnowLocation()
                whenLoading.postValue(true)
                val request = ActionJobRequest.build(
                    type = type ?: "",
                    main_id = main_id ?: "",
                    lat = lat,
                    lng = lng,
                    dateHold = "",
                    remark = "" ?: ""
                )

                apiManager.setActionJob(request) { isError: Boolean, result: String ->
                    whenLoading.postValue(false)

                    if (isError) {
                        if (result.equals("overtime")) {
                            whenOvertime.postValue(true)
                        } else {
                            whenSendToApiFailure.postValue(result)
                        }
                        return@setActionJob
                    }
                    whenSendToApiSuccess.postValue(result)
                }
            } catch (e: Exception) {
                e.message
            }
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


}