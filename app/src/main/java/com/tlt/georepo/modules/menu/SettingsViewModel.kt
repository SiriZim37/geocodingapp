package com.tlt.georepo.modules.menu

import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.tlt.georepo.common.base.BaseViewModel
import com.tlt.georepo.common.exception.ApiException
import com.tlt.georepo.common.livedata.SingleLiveData
import com.tlt.georepo.manager.JsonMapperManager
import com.tlt.georepo.manager.LocationManager
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.model.request.*
import com.tlt.georepo.model.response.RegistProfileResponse
import com.tlt.georepo.util.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Observer
import kotlin.coroutines.suspendCoroutine

class SettingsViewModel : BaseViewModel() {
    val whenDataLoaded = MutableLiveData<Boolean>()
    val whenDataLoadedLocationSuccess = MutableLiveData<LatLng>()
    val updateProfileSuccess = MutableLiveData<Boolean>()
    val isApproved = SingleLiveData<Boolean>()
    val isNotApproved = SingleLiveData<Boolean>()
    private val apiManager by lazy { GeoApiManager.getInstance() }
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

    fun updateProfileImage(imageUri: Uri? = Uri.EMPTY) {
        if (imageUri == null || imageUri == Uri.EMPTY) {
            return
        }

        GlobalScope.launch(Dispatchers.Main) {
            val base64 = ImageUtils.encodeToBase64(imageUri)
            val request = UploadImagesRequest.buildForImageProfile(base64)
            apiManager.uploadImages(request) { isError, result ->
                try {
                    if (isError) {
                        return@uploadImages
                    }
                    whenDataLoaded.postValue(true)
                    return@uploadImages
                } catch (e: Exception) {
                    e.message
                }

            }
        }
    }

    fun updateUserProfile(lat: String, lng: String, email: String, phone: String, lang: String , offline:String) {
        val request = UpdateUserProfileRequest.build(
            lat = lat,
            lng = lng ,
            email = email ,
            phone = phone,
            lang = lang,
            offline = offline
        )
        apiManager.getUpdateUserProfile(request) { isError: Boolean, result: String ->
            try {
                if (isError) {
                    updateProfileSuccess.postValue(false)
                    return@getUpdateUserProfile
                }
                try {
                    if (result.equals("success")) {
                        updateProfileSuccess.postValue(true)
                    }else{
                        updateProfileSuccess.postValue(false)
                    }
                } catch (e: java.lang.Exception) {
                    Log.d("Error Connect API : ", e.stackTrace.toString())
                    Log.d("Error Connect API 2 : ", e.toString() + " and " + e.printStackTrace())
                }
                return@getUpdateUserProfile
            } catch (e: java.lang.Exception) {
                Log.e("getPullJobCollection : ", e.stackTrace.toString())
            }
        }
    }
}
