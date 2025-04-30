package com.tlt.georepo.modules.main

import android.arch.lifecycle.MutableLiveData
import android.provider.ContactsContract
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.iid.FirebaseInstanceId
import com.tlt.georepo.common.base.BaseViewModel
import com.tlt.georepo.common.extension.decodeBase64
import com.tlt.georepo.common.extension.unzip
import com.tlt.georepo.manager.JsonMapperManager
import com.tlt.georepo.manager.LocationManager
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.manager.db.MasterDataManager
import com.tlt.georepo.manager.db.UserManager
import com.tlt.georepo.model.entity.masterdata.MasterData
import com.tlt.georepo.model.request.GetMasterDataRequest
import com.tlt.georepo.model.request.NonCustomerRequest
import com.tlt.georepo.model.request.RegisterNonCustRequest
import com.tlt.georepo.model.request.UpdateMasterDataRequest
import com.tlt.georepo.model.response.CheckMasterDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.google.firebase.perf.FirebasePerformance




class InfoViewModel : BaseViewModel() {
    val whenUserIsNotCustomer = MutableLiveData<Boolean>()
    val whenUserIsCustomer = MutableLiveData<Boolean>()
    val whenUserIsNonCust = MutableLiveData<Boolean>()
    val whenUserIsNotNonCust = MutableLiveData<Boolean>()
    val whenLoadCustomer = MutableLiveData<Boolean>()
    val whenLoadMaster = MutableLiveData<Boolean>()
    val whenDataLoadedLocationSuccess = MutableLiveData<LatLng>()
    val whenLoadMasterFail = MutableLiveData<Boolean>()
    private val apiManager by lazy { GeoApiManager.getInstance() }

    private lateinit var checkMasterDataResponse: CheckMasterDataResponse
//    val whenLoading = MutableLiveData<Boolean>()

//    fun checkStatusLoginUser() {
//        whenLoading.postValue(true)
//        Log.e("checkStatusLoginUser", "checkStatusLoginUser")
//        try {
//            val userRegis = DatabaseManager.getInstance().getUserInfo()
//            if (userRegis.flagLogin) {
//                whenUserIsCustomer.postValue(true)
//            }
//            else {
//                whenUserIsNotCustomer.postValue(true)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            whenUserIsNotCustomer.postValue(false)
//        }
//
//    }
//

    fun getIntialByApi(lat: String, lng: String) {
        whenLoading.postValue(true)
        if (FirebaseInstanceId.getInstance().token == null) {
            return
        }
        val userRegis = UserManager.getInstance()
        val myTrace = FirebasePerformance.getInstance().newTrace("regis_fetch")
        if (userRegis.isRegisteredNonCust()) {
            myTrace.start();
            checkMasterData(false)
            myTrace.stop();
//            checkStatusLoginUser()
        } else {
            Log.e("RegisNonCust----", "-----")

            myTrace.start();

            RegisNonCust(lat, lng)
            myTrace.stop();

        }

    }

    private fun sendUpdateMasterDataSuccessToApi() {
        var currentVersion = MasterDataManager.getInstance().getCurrentVersion()
        if (currentVersion.isEmpty()) {
            currentVersion = checkMasterDataResponse.filename
        }
        val request = UpdateMasterDataRequest.build(currentVersion)

        GeoApiManager.getInstance().updateMasterData(request) { isError, result ->
            if (isError) {
                return@updateMasterData
            }

            MasterDataManager.getInstance()
                .updateVersion(checkMasterDataResponse.filename)

//            masterDataLastestVersion()
        }
    }

//    private fun masterDataLastestVersion() {
//        val user = UserManager.getInstance()
//
//        whenLoading.postValue(false)
//
////        if (user.isCustomer()) {
////            whenUserIsCustomer.postValue(true)
////        } else {
////            whenUserIsNotCustomer.postValue(user.isShowAppIntro())
////        }
//    }


    fun RegisNonCust(lat: String, lng: String) {
        val request = RegisterNonCustRequest.build(
            lat = lat,
            lng = lng
        )
        apiManager.getRegistNonCust(request) { isError: Boolean, result: String ->
            try {
                if (isError) {
                    return@getRegistNonCust
                }
                try {
                    if (result.equals("success")) {
                        checkMasterData(true)
//                        whenLoadCustomer.postValue(true)
                    }
                } catch (e: Exception) {
                    Log.d("Error Connect API : ", e.stackTrace.toString())
                    Log.d("Error Connect API 2 : ", e.toString() + " and " + e.printStackTrace())
                }
                return@getRegistNonCust
            } catch (e: Exception) {
                Log.e("getPullJobCollection : ", e.stackTrace.toString())
            }
        }
    }


    private fun checkMasterData(isRegisted: Boolean) {
        Log.e("checkMasterData", "checkMasterData")
        whenLoading.postValue(false)
        val version = MasterDataManager.getInstance().getCurrentVersion()

        GeoApiManager.getInstance()
            .getMasterData(GetMasterDataRequest.build(version)) { isError, result ->
                if (isError) {
                    if (result == "noInternet") {
                        whenNoInternetConnection.postValue(true)
                    }
                    return@getMasterData
                }

                val items = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<CheckMasterDataResponse>::class.java)

                if (items == null || items.isEmpty()) {
                    whenLoadMasterFail.postValue(true)
                    return@getMasterData
                }

                checkMasterDataResponse = items.first()
                MasterDataManager.getInstance().getSubMitList()
                if (checkMasterDataResponse.filename.isEmpty()) {
//                    masterDataLastestVersion()
                    return@getMasterData
                }

                saveMasterData(checkMasterDataResponse)
                Log.e("whenLoadMaster", "whenLoadMaster")
//                if (isRegisted) {
//                    whenLoadMaster.postValue(true)
//                }
//
                whenLoadMaster.postValue(true)
                return@getMasterData
            }
    }


    private fun saveMasterData(checkMasterDataResponse: CheckMasterDataResponse) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val json = upzipMasterDataBase64(checkMasterDataResponse.base64)
                saveMasterDataIntoDB(json)
                sendUpdateMasterDataSuccessToApi()
            } catch (e: Exception) {
                e.message
            }

        }
    }

    private suspend fun upzipMasterDataBase64(base64: String?) = suspendCoroutine<String> {
        base64?.ifEmpty {
            it.resume("")
            return@suspendCoroutine
        }

        val json = base64?.decodeBase64()?.unzip() ?: ""

        it.resume(json)
    }

    private fun saveMasterDataIntoDB(json: String) {
        val masterData = JsonMapperManager.getInstance()
            .gson.fromJson(json, MasterData::class.java)
        DatabaseManager.getInstance().saveMasterData(masterData)

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
