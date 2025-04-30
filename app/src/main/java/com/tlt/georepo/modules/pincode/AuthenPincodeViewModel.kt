package com.tlt.georepo.modules.pincode

import android.app.AlertDialog
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import com.tlt.georepo.R
import com.tlt.georepo.manager.ContextManager
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.manager.db.UserManager
import com.tlt.georepo.model.request.NonCustomerRequest
import com.tlt.georepo.util.LocaleManager
import java.lang.Exception
import android.os.Build
import android.annotation.TargetApi
import android.content.res.Configuration
import android.support.annotation.NonNull
import com.google.android.gms.maps.model.LatLng
import com.tlt.georepo.common.eventbus.SendLocationEvent
import com.tlt.georepo.manager.LocationManager
import com.tlt.georepo.model.request.LoginByDeviceRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class AuthenPincodeViewModel : ViewModel() {

    private val userManager = UserManager.getInstance()
    var pincodeInvalidMessage = ""
//            = ContextManager.getInstance().getStringByRes(R.string.pincode_incorrect)
    val pincode3TimesInvalidMessage = ContextManager.getInstance().getStringByRes(R.string.pincode_incorrect_3_times)
    val pincode4TimesInvalidMessage = ContextManager.getInstance().getStringByRes(R.string.pincode_incorrect_4_times)
    val authAttemptOverLimitMessage =
        ContextManager.getInstance().getStringByRes(R.string.auth_attempt_over_limit_dialog_description)
    private val apiManager by lazy { GeoApiManager.getInstance() }
    val whenLoading = MutableLiveData<Boolean>()
    val whenAuthSuccess = MutableLiveData<String>()
    val whenAuthFailure = MutableLiveData<String>()
    val whenOTPAuthFailure = MutableLiveData<String>()
    val whenAuthAttemptOverLimit = MutableLiveData<Boolean>()
    val whenShowFingerprintAuth = MutableLiveData<Boolean>()

    val whenDataLoadedLocationSuccess = MutableLiveData<LatLng>()

    fun loginByPincode(pincode: String , lat : String , lng: String) {
        val hash = UserManager.getInstance().hashPincodeForAuth(pincode)
        Log.e("hashPincodeForAuth",hash)
        setLanguage(R.string.pincode_incorrect)
        setLogin(hash ,lat , lng )
    }

    private fun setLanguage(id: Int) {
        Log.e("sharedPref_lang", LocaleManager.getLanguagePref(ContextManager.getInstance().getApplicationContext()))
        var sharedPref_lang = LocaleManager.getLanguagePref(ContextManager.getInstance().getApplicationContext())
        pincodeInvalidMessage = getStringByLocal(ContextManager.getInstance().getApplicationContext(),id,sharedPref_lang)
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun getStringByLocal(context: Context, id: Int, locale: String): String {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(Locale(locale))
        return context.createConfigurationContext(configuration).resources.getString(id)
    }

    fun verifyOTP(otpCode: String) {
        if (otpCode.length == 6) {
            apiVerifyOTP(otpCode)
        } else {
            whenAuthFailure.postValue("fail")
        }
    }

    fun apiVerifyOTP(otpCode: String) {
        apiManager.getVerifyOTP(NonCustomerRequest().getVerifyOTPRequestBody(otpCode)) { isError: Boolean, result: String ->
            try {
                if (isError) {
                    whenAuthFailure.postValue("fail")
                    return@getVerifyOTP
                }
                try {
                    Log.e("apiVerifyOTP", result)
                    if (result.equals("Y")) {
                        whenAuthSuccess.postValue("success")
                    } else {
                        whenAuthFailure.postValue("fail")
                    }
                } catch (e: Exception) {
                    Log.d("Error Connect API : ", e.stackTrace.toString())
                    Log.d("Error Connect API 2 : ", e.toString() + " and " + e.printStackTrace())
                }
                return@getVerifyOTP
            } catch (e: Exception) {
                Log.e("getPullJobCollection : ", e.stackTrace.toString())
            }
        }
    }


    private fun setLogin(pincode: String ,
                         lat: String ,
                         lng: String) {
        var request = LoginByDeviceRequest.build(
            pincode = pincode ,
            lat = lat ,
            lng = lng
        )
        apiManager.setLogin(request) { isError, result ->
            try {
                if (isError) {
                    Log.e("setLogin_error_setLogin", result)
                    whenAuthFailure.postValue(pincodeInvalidMessage)
                    return@setLogin
                }
                try {
                    Log.e("setLogin_success", result)
                    if (result.equals("success")) {
                        whenAuthSuccess.postValue("success")
                    } else {
                        Log.e("setLogin_error_setLogin", result)
                        whenAuthFailure.postValue("fail")
                    }

                } catch (e: Exception) {
                    Log.d("Error Connect API : ", e.stackTrace.toString())
                    Log.d("Error Connect API 2 : ", e.toString() + " and " + e.printStackTrace())
                }
                return@setLogin
            } catch (e: Exception) {
                whenAuthFailure.postValue(pincodeInvalidMessage)
                Log.e("setLogin_exception : ", e.stackTrace.toString())
            }
        }
    }

    fun showAlert(context: Context) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage("Please ensure that you you have entered all the required information")
            .setCancelable(false)
            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
            })
//            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
//                    dialog, id -> dialog.cancel()
//            })
        val alert = dialogBuilder.create()
//        alert.setTitle("Please ensure that you you have entered all the required information")
        alert.show()
    }
//    fun checkFingerprintSetting() {
//        if (!Reprint.isHardwarePresent()
//            || !Reprint.hasFingerprintRegistered()
//            || !userManager.getProfile().isEnableFingerprintAuth) {
//            return
//        }
//
//        whenShowFingerprintAuth.postValue(true)
//    }
//
//    fun checkLoginAttemptOverLimit() {
//        if (!userManager.isLoginAttemptOverLimit()) {
//            return
//        }
//
//        whenAuthFailure.postValue(authAttemptOverLimitMessage)
//        whenAuthAttemptOverLimit.postValue(true)
//    }
//
//    fun loginByPincode(pincode: String) {
//        whenLoading.postValue(true)
//
//        GlobalScope.launch(Dispatchers.Main) {
//            val hash = hashAuthPincode(pincode)
//            val currentLocation = LocationManager.getLastKnowLocation()
//            val request = LoginByCustomerRequest.buildForPincode(pincodeWithHash = hash,
//                latitude = currentLocation.latitude.toString(),
//                longitude = currentLocation.longitude.toString())
//
//            TLTApiManager.getInstance()
//                .loginByCustomer(request) { isError, result ->
//                    whenLoading.postValue(false)
//
//                    if (!isError) {
//                        userManager.resetLoginAttempt()
//                        whenAuthSuccess.postValue(pincode)
//                        return@loginByCustomer
//                    }
//
//                    whenAuthFailure.postValue(pincodeInvalidMessage)
//                    if (result != "noInternet") {
//                        userManager.increaseLoginAttempt()
//                    }
//
//                    if (userManager.isLoginAttemptOverLimit()) {
//                        whenAuthAttemptOverLimit.postValue(true)
//                        return@loginByCustomer
//                    }
//
//                    val attempt = userManager.getLoginAttempt()
//
//                    if (attempt == 3) {
//                        whenAuthFailure.postValue(pincode3TimesInvalidMessage)
//                    }
//
//                    if (attempt == 4) {
//                        whenAuthFailure.postValue(pincode4TimesInvalidMessage)
//                    }
//                }
//        }
//    }
//
//    fun loginByFingerprint() {
//        GlobalScope.launch(Dispatchers.Main) {
//            val currentLocation = LocationManager.getLastKnowLocation()
//            val request = LoginByCustomerRequest.buildForFingerprint(
//                latitude = currentLocation.latitude.toString(),
//                longitude = currentLocation.longitude.toString())
//
//            whenLoading.postValue(true)
//
//            TLTApiManager.getInstance()
//                .loginByCustomer(request) { isError, result ->
//                    whenLoading.postValue(false)
//
//                    when (isError) {
//                        true -> whenAuthFailure.postValue(result)
//                        else -> whenAuthSuccess.postValue(result)
//                    }
//                }
//        }
//    }
//
//    private suspend fun hashAuthPincode(pincode: String) = suspendCoroutine<String> {
//        if (pincode.isEmpty()) {
//            it.resume("")
//            return@suspendCoroutine
//        }
//
//        val hash = UserManager.getInstance().hashPincodeForAuth(pincode)
//        it.resume(hash)
//    }

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

    fun flagCheckLogin(pin: String): Boolean {
        var result = false
        try {
            if (pin == DatabaseManager.getInstance().GetPinCode()?.pincode!!) {
                result = true
            } else {
                result = false
            }
        } catch (e: Exception) {
            result = false
        }
        return result
    }
}