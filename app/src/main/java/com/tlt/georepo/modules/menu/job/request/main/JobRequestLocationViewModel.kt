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
import com.tlt.georepo.model.entity.location.LocationDetailEntity
import com.tlt.georepo.model.request.PullJobCollectionRequest
import com.tlt.georepo.model.response.ItemPullJobCollectionResponse
import com.tlt.georepo.util.PermissionUtils
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import com.tlt.georepo.common.exception.ApiException
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.model.request.SetDataLocationRequest

class JobRequestLocationViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()
    val whenDataLoadedNoData = MutableLiveData<Boolean>()
    val whenDataLoadedJobDataAll = MutableLiveData<JobModel>()
    val whenDataLoadedSuccess = MutableLiveData<JobModel>()
    val whenOvertime = MutableLiveData<Boolean>()
    var isOvertime: Boolean = false

    private val NEARBY_LIMIT_ITEM = 10
    private val apiManager by lazy { GeoApiManager.getInstance() }
    private var job_list: List<JobPullCollection>? = null

    fun getDataJobAll(lat: String, lng: String) {
        whenLoading.postValue(true)
        GlobalScope.launch(Dispatchers.Main) {
            try {

                val dataS = getPullJobCollection(lat, lng) // getPullJobCollection()

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
            whenDataLoadedJobDataAll.postValue(
                JobModel(
                    jobLocationList = locationList ?: listOf(),
                    jobLocationType = Type.CUSTHOUSE
                )
            )
        }
    }

    private suspend fun getCustHouseLocationsByNearly(): List<JobPullCollection>? {
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
        JobList: List<JobPullCollection>?
    ): List<JobPullCollection>? {
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
            JobPullCollection(
                aDDRESSID = it.aDDRESSID ?: "",
                bRANCH = it.bRANCH ?: "",
                cONTRACTNO = it.cONTRACTNO,
                cUSTOMERNAME = it.cUSTOMERNAME,
                iDCARD = it.iDCARD,
                iNSTALLMENTDUEAMT = it.iNSTALLMENTDUEAMT ?: "",
                oVERDUE_ITEM = it.oVERDUE_ITEM ?: "",
                jOBID = it.jOBID ?: "",
                lATITUDE = it.lATITUDE ?: "",
                lONGITUDE = it.lONGITUDE ?: "",
                mAINID = it.mAINID ?: "",
                pROFILEID = it.pROFILEID ?: "",
                rEALADDRESS = it.rEALADDRESS,
                rEGISTERNO = it.rEGISTERNO ?: "",
                rEGISTERPROVINCE = it.rEGISTERPROVINCE ?: "",
                distance = distance_km,
                oVERDUE_DAYS = it.oVERDUE_DAYS,
                cOLOR_DESC = it.cOLOR_DESC,
                mODEL_DESC = it.mODEL_DESC,
                rECORD_TYPE = it.rECORD_TYPE,
                dISTANCT_KM = it.dISTANCT_KM ?: "",
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

            locations?.set(0, selectedItem ?: JobPullCollection())
            locations?.set(index, firstItem ?: JobPullCollection())

            model?.jobLocationList = locations?.toList() ?: listOf()

            whenDataLoadedJobDataAll.postValue(model)
        }
    }

    fun saveLocationDetail(item: JobPullCollection) {
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
            overdueitem = item.oVERDUE_ITEM ?: "",
            overdueday = item.oVERDUE_DAYS ?: "",
            colordesc = item.cOLOR_DESC ?: "",
            modeldesc = item.mODEL_DESC ?: "",
            recordtype = item.rECORD_TYPE ?: "",
            distance = item.dISTANCT_KM ?: ""

        )

        LocationManager.saveLocationDetail(locationDetail)
    }

    data class Model(
        var locationList: List<Location> = listOf(),
        val locationType: Type = Type.CUSTHOUSE
    )

    data class Location(
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
        val oVERDUE_DAYS: String = "",
        val cOLOR_DESC: String = "",
        val mODEL_DESC: String = "",
        val rECORD_TYPE: String = "",
        val type: Type = Type.CUSTHOUSE
    )

    enum class Type {
        CUSTHOUSE,
        NEARBY
    }

    data class JobModel(
        var jobLocationList: List<JobPullCollection> = listOf(),
        val jobLocationType: Type = Type.CUSTHOUSE
    )

    data class JobPullCollection(
        val aDDRESSID: String = "",
        val bRANCH: String = "",
        val cONTRACTNO: String = "",
        val cUSTOMERNAME: String = "",
        val iDCARD: String = "",
        val iNSTALLMENTDUEAMT: String = "",
        val oVERDUE_ITEM: String = "",
        val oVERDUE_DAYS: String = "",
        val cOLOR_DESC: String = "",
        val mODEL_DESC: String = "",
        val rECORD_TYPE: String = "",
        val jOBID: String = "",
        val lATITUDE: String = "",
        val lONGITUDE: String = "",
        val mAINID: String = "",
        val pROFILEID: String = "",
        val rEALADDRESS: String = "",
        val rEGISTERNO: String = "",
        val rEGISTERPROVINCE: String = "",
        val distance: String = "",
        val dISTANCT_KM: String = "",
        val type: Type = Type.CUSTHOUSE
    )

    /*    -------- CALL SERVICE API --------  */

    private suspend fun getPullJobCollection(lat: String, lng: String) =
        suspendCoroutine<ItemPullJobCollectionResponse?> {
            val latitude = lat
            val longtitude = lng
            val request = PullJobCollectionRequest.build(latitude, longtitude)
            apiManager.getPullJobCollection(request) { isError: Boolean, result: String ->
                try {
                    if (isError) {
                        if (result.equals("overtime")) {
                            whenOvertime.postValue(true)
                        }else{
                            it.resumeWithException(Exception(result))
                        }
                        return@getPullJobCollection
                    }
                    try {
                        Log.e("getPullJobCollection  ", result)
                        val items = JsonMapperManager.getInstance()
                            .gson.fromJson(result, Array<ItemPullJobCollectionResponse>::class.java)
                            .toList()
                            .map {
                                JobPullCollection(
                                    aDDRESSID = it.aDDRESSID
                                    , bRANCH = it.bRANCH
                                    , cONTRACTNO = it.cONTRACTNO
                                    , cUSTOMERNAME = it.cUSTOMERNAME
                                    , iDCARD = it.iDCARD
                                    , iNSTALLMENTDUEAMT = it.iNSTALLMENTDUEAMT
                                    , oVERDUE_ITEM = it.oVERDUE_ITEM
                                    , oVERDUE_DAYS = it.oVERDUE_DAYS
                                    , mODEL_DESC = it.mODEL_DESC
                                    , cOLOR_DESC = it.cOLOR_DESC
                                    , rECORD_TYPE = it.rECORD_TYPE
                                    , jOBID = it.jOBID
                                    , lATITUDE = it.lATITUDE
                                    , lONGITUDE = it.lONGITUDE
                                    , mAINID = it.mAINID
                                    , pROFILEID = it.pROFILEID
                                    , rEALADDRESS = it.rEALADDRESS
                                    , rEGISTERNO = it.rEGISTERNO
                                    , rEGISTERPROVINCE = it.rEGISTERPROVINCE
                                    , dISTANCT_KM = it.dISTANCT_KM
                                )
                            }
                            .sortedBy {
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
                    return@getPullJobCollection
                } catch (e: Exception) {
                    Log.e("getPullJobCollection : ", e.printStackTrace().toString())
                }
            }
        }


}