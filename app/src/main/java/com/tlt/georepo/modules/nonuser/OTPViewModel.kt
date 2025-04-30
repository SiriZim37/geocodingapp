package com.tlt.georepo.modules.nonuser

import android.annotation.TargetApi
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.support.annotation.NonNull
import android.util.Log
import com.tlt.georepo.R
import com.tlt.georepo.manager.ContextManager
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.model.request.RegistPost
import com.tlt.georepo.model.request.NonCustomerRequest
import com.tlt.georepo.util.LocaleManager
import java.lang.Exception
import java.util.*

class OTPViewModel : ViewModel() {
    private val apiManager by lazy { GeoApiManager.getInstance() }
    var request = RegistPost()
    var errorMessage = ""
    var isSuccessOTP = MutableLiveData<Boolean>()
    var isFailedOTP = MutableLiveData<Boolean>()
    val whenLoading = MutableLiveData<Boolean>()
    val whenAuthOTPSuccess = MutableLiveData<String>()
    val whenAuthOTPFailure = MutableLiveData<String>()
    private fun setLanguage(id: Int) {
        Log.e("sharedPref_lang", LocaleManager.getLanguagePref(ContextManager.getInstance().getApplicationContext()))
        var sharedPref_lang = LocaleManager.getLanguagePref(ContextManager.getInstance().getApplicationContext())
        errorMessage = getStringByLocal(ContextManager.getInstance().getApplicationContext(),id,sharedPref_lang)
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun getStringByLocal(context: Context, id: Int, locale: String): String {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(Locale(locale))
        return context.createConfigurationContext(configuration).resources.getString(id)
    }

    fun verifyOTP(otpCode: String) {
        setLanguage(R.string.error_otp)
        if (otpCode.length == 6) {
            apiVerifyOTP(otpCode)
        } else {
            whenAuthOTPFailure.postValue(errorMessage)
        }
    }

    fun apiVerifyOTP(otpCode: String) {
        apiManager.getVerifyOTP(NonCustomerRequest().getVerifyOTPRequestBody(otpCode)) { isError: Boolean, result: String ->
            try {
                if (isError) {
                    whenAuthOTPFailure.postValue(errorMessage)
                    return@getVerifyOTP
                }
                try {
                    Log.e("apiVerifyOTP", result)
                    if (result.equals("Y")) {
                        whenAuthOTPSuccess.postValue("success")
                    } else {
                        whenAuthOTPFailure.postValue(errorMessage)
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

    fun apiSendOTP(phone :String) {
        apiManager.getSendOTP(NonCustomerRequest().getOTPRequestBody(phone)) { isError: Boolean, result: String ->
            try {
                if (isError) {
                    isFailedOTP.postValue(true)
                    Log.e("isFailedOTP "  ,result )
                    return@getSendOTP
                }
                try {
                    isSuccessOTP.postValue(true)
                } catch (e: Exception) {
                    Log.d("Error Connect API : ", e.stackTrace.toString())
                    Log.d("Error Connect API 2 : ", e.toString() + " and " + e.printStackTrace())
                }
                return@getSendOTP
            } catch (e: Exception) {
                Log.e("getPullJobCollection : ", e.stackTrace.toString())
            }
        }
    }




}