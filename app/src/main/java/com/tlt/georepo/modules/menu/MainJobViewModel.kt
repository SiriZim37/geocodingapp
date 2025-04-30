package com.tlt.georepo.Fragment


import android.annotation.TargetApi
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.support.annotation.NonNull
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.tlt.georepo.common.base.BaseViewModel
import com.tlt.georepo.common.livedata.SingleLiveData
import com.tlt.georepo.manager.ContextManager
import com.tlt.georepo.manager.JsonMapperManager
import com.tlt.georepo.manager.LocationManager
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.model.request.RegistProfileRequest
import com.tlt.georepo.model.response.RegistProfileResponse
import com.tlt.georepo.modules.menu.SettingsViewModel
import com.tlt.georepo.modules.menu.common.DataProfileItem
import com.tlt.georepo.util.LocaleManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.suspendCoroutine

class MainJobViewModel : BaseViewModel() {
    val whenDataLoaded = MutableLiveData<String>()
    val whentest = MutableLiveData<RegistProfileResponse>()
    val whenDataLoadedLocationSuccess = MutableLiveData<LatLng>()
    val whenDataLoadedProfileError = MutableLiveData<Boolean>()
    val whenDataLoadedProfileSuccess = MutableLiveData<DataProfileItem>()
    val isApproved = SingleLiveData<Boolean>()
    val isNotApproved = SingleLiveData<Boolean>()
    var errorMessage = ""
    lateinit var list_custPofile: DataProfileItem
    private val apiManager by lazy { GeoApiManager.getInstance() }
    private var profile_list : List<RegistProfileResponse>? = null
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

    fun getDataProfile(lat: String, long: String) {
        whenLoading.postValue(true)

        GlobalScope.launch(Dispatchers.Main) {
            try {
                getRegistProfile(lat, long)
            } catch (e: Exception) {
                whenDataFailure.postValue(e.message)
            } finally {
                whenLoading.postValue(false)
            }
        }

    }

    private suspend fun getRegistProfile(lat: String, lng: String) = suspendCoroutine<RegistProfileResponse?> {
        val request = RegistProfileRequest.build(
            lat = lat,
            lng = lng
        )
        apiManager.getRegistProfile(request) { isError: Boolean, result: String ->
            try {
                if (isError) {
                    whenDataLoadedProfileError.postValue(true)
                    return@getRegistProfile
                }
                try {
                    Log.e("getRegistProfile_setting", result)
                    val items = JsonMapperManager.getInstance()
                        .gson.fromJson(result, RegistProfileResponse::class.java)

                    var detail = DataProfileItem(
                        regisid = items.regisid!!,
                        regisname = items.regisname!!,
                        regissurname = items.regissurname!!,
                        profileimg = items.profileimg!!,
                        flagapprove = items.flagapprove!!,
                        approvedesc = items.approvedesc!!,
                        Mobile = items.Mobile!!,
                        Email = items.Email!!,
                        startworkingdate = items.startworkingdate!!,
                        completiondate = items.completiondate!!,
                        certimg = items.certimg!!,
                        offlinemode = items.offlinemode!!,
                        flaglanguage = items.flaglanguage!!,
                        compcode = items.compcode!!,
                        companyth = items.companyth!!
                    )
                    Log.e("getRegistProfile", items.Mobile)

                    list_custPofile = detail

                    whenDataLoadedProfileSuccess.postValue(detail)
                } catch (e: java.lang.Exception) {
                    whenDataLoadedProfileError.postValue(true)
                    Log.d("Error_Connect_API : ", e.stackTrace.toString())
                    Log.d("Error_Connect_API 2 : ", e.toString() + " and " + e.printStackTrace())
                }
                return@getRegistProfile
            } catch (e: java.lang.Exception) {
                whenDataLoadedProfileError.postValue(true)
                Log.e("getRegistProfile : ", e.stackTrace.toString())
            }
        }
    }

    fun setLanguage(id: Int): String {
        Log.e("sharedPref_lang", LocaleManager.getLanguagePref(ContextManager.getInstance().getApplicationContext()))
        var sharedPref_lang = LocaleManager.getLanguagePref(ContextManager.getInstance().getApplicationContext())
        errorMessage = getStringByLocal(ContextManager.getInstance().getApplicationContext(),id,sharedPref_lang)
        return errorMessage
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun getStringByLocal(context: Context, id: Int, locale: String): String {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(Locale(locale))
        return context.createConfigurationContext(configuration).resources.getString(id)
    }




}
