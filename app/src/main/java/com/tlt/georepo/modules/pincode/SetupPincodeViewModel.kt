package com.tlt.georepo.modules.pincode

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.support.annotation.NonNull
import android.util.Log
import com.google.gson.Gson
import com.tlt.georepo.manager.ContextManager
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.manager.db.UserManager
import com.tlt.georepo.model.entity.CurrentLocationNonCust
import com.tlt.georepo.model.entity.UserInfo
import com.tlt.georepo.model.request.SetPINRegisRequest
import com.tlt.georepo.model2.entity.UserPinCode
import com.tlt.georepo.util.LocaleManager
import java.util.*

class SetupPincodeViewModel : ViewModel() {

    val whenLoading = MutableLiveData<Boolean>()
    val whenPasswordNotMatchMessage = MutableLiveData<Boolean>()
    val whenPasswordRequire6DigitsMessage = MutableLiveData<Boolean>()
    val whenValidatePasswordSuccess = MutableLiveData<Boolean>()
    val whenValidatePasswordFailure = MutableLiveData<String>()
    val whenSetupPincodeSuccess = MutableLiveData<Boolean>()
    val whenSetupPincodeFailure = MutableLiveData<Boolean>()
    var status = 0
    var errorMessage = ""

    private val apiManager = GeoApiManager.getInstance()

    fun sendPincodeToApi(pincode: String , isForgotPin: Boolean) {
        val hash = UserManager.getInstance().hashPincodeForSetup(pincode)
        Log.e("setPinRegist", pincode)
        Log.e("setPinRegist", hash)
        if( isForgotPin ){
            val request = SetPINRegisRequest.buildForForgotPincode(hash)
            setPinRegist(request)
        }else {
            val request = SetPINRegisRequest.buildForRegister(hash)
            setPinRegist(request)
        }

    }


    fun validatePassword(password: String, confirmPassword: String, isForgotPin :Boolean) {
        if (password.length < 6) {
            whenPasswordNotMatchMessage.postValue(true)
            return
        }

        if (password != confirmPassword) {
            whenPasswordNotMatchMessage.postValue(true)
            return
        }
        if (password.length == 6) {
            sendPincodeToApi(password , isForgotPin)
        }
    }

    /* ------------- CALL API HERE ----------  */

    private fun setPinRegist(request: SetPINRegisRequest) {
        apiManager.setPinRegister(request) { isError, result ->
            try {
                if (isError) {
                    Log.e("setPinRegist", result)
                    whenValidatePasswordFailure.postValue("เกิดข้อผิดพลาด โปรดลองใหม่อีกครั้ง")
                    DatabaseManager.getInstance().deleteUserInfo()
                    return@setPinRegister
                }
                try {
                    Log.e("setPinRegist_success", result)
                    if (result.equals("success")) {
                        val item = UserInfo().apply {
                            flagLogin  = true
                        }
                        DatabaseManager.getInstance().updateFlaglogin(item)
//                        DatabaseManager.getInstance().saveisForgot(false)
                        whenValidatePasswordSuccess.postValue(true)
                    } else {

                            DatabaseManager.getInstance().deleteUserInfo()
                        whenValidatePasswordFailure.postValue("เกิดข้อผิดพลาด โปรดลองใหม่อีกครั้ง")

                    }

                } catch (e: Exception) {

                        DatabaseManager.getInstance().deleteUserInfo()
                    Log.d("Error Connect API : ", e.stackTrace.toString())
                    Log.d("Error Connect API 2 : ", e.toString() + " and " + e.printStackTrace())
                }
                return@setPinRegister
            } catch (e: Exception) {

                    DatabaseManager.getInstance().deleteUserInfo()
                whenValidatePasswordFailure.postValue("เกิดข้อผิดพลาด โปรดลองใหม่อีกครั้ง")
            }
        }
    }


    fun sendPincodeToSave(userid: String, pinCode: String) {
        val entity = UserPinCode().apply {
            userID = userid
            pincode = pinCode
        }
        DatabaseManager.getInstance().saveUserPincode(entity)
        whenSetupPincodeSuccess.postValue(true)
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