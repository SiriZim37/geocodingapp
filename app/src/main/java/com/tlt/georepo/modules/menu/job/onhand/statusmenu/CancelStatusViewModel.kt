package com.tlt.georepo.modules.menu.job.onhand.statusmenu

import android.arch.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.tlt.georepo.common.base.BaseViewModel
import com.tlt.georepo.manager.LocalizeManager
import com.tlt.georepo.manager.LocationManager
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.manager.db.MasterDataManager
import com.tlt.georepo.manager.db.UserManager
import com.tlt.georepo.model.request.ActionJobRequest
import com.tlt.georepo.util.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CancelStatusViewModel : BaseViewModel() {
    val whenDataLoaded = MutableLiveData<ArrayList<RejectJob>>()
    val whenSendToApiFailure = MutableLiveData<String>()
    val whenSendToApiSuccess = MutableLiveData<String>()
    private val apiManager by lazy { GeoApiManager.getInstance() }
    private val userManager = UserManager.getInstance()
    val whenDataLoadedLocationSuccess = MutableLiveData<LatLng>()
    private val rejectJobList = ArrayList<RejectJob>()
    val whenOvertime = MutableLiveData<Boolean>()
    var isOvertime: Boolean = false

    fun getDataMaster(language: String) {
        if (rejectJobList.isEmpty()) {
            if (language == "TH") {
                val rejectMaster = MasterDataManager.getInstance()
                    .getRegectList()?.map {
                        RejectJob(it.mSVALUE!!, it.mSDESCTH.toString())
                    }
                rejectJobList.addAll(rejectMaster!!)
                whenDataLoaded.postValue(rejectJobList)
//                whenSubmitLoaded.postValue(submitJobList.map { it.MS_DESC })
            } else {
                val rejectMaster = MasterDataManager.getInstance()
                    .getRegectList()?.map {
                        RejectJob(it.mSVALUE!!, it.mSDESCEN!!)
                    }
                rejectJobList.addAll(rejectMaster!!)
                whenDataLoaded.postValue(rejectJobList)
//                whenSubmitLoaded.postValue(submitJobList.map { it.MS_DESC})
            }
        }

    }


    fun sendActionJob(
        type: String,
        main_id: String,
        lat: String,
        lng: String,
        remark: String
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
                    remark = remark
                )

                apiManager.setActionJob(request) { isError: Boolean, result: String ->
                    whenLoading.postValue(false)

                    if (isError) {
//                        isOvertime = DatabaseManager.getInstance().getOvertime().isOverTime
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


    data class RejectJob(
        val MS_VALUE: String = "",
        val MS_DESC: String = ""
    )


    data class Model(
        val name: String = "",
        val email: String = "",
        val phonenumber: String = "",
        val carLicense: String = "",
        val topics: List<String> = listOf(),
        val status: Status = Status.NON_CUSTOMER
    )

    enum class Status {
        CUSTOMER,
        NON_CUSTOMER
    }
}