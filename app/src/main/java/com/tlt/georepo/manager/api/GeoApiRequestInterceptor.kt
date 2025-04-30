package com.tlt.georepo.manager.api

import android.content.Context
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.tlt.georepo.BuildConfig
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.manager.db.SharedPref
import com.tlt.georepo.manager.db.UserManager
import com.tlt.georepo.util.PreferenceHelper
import okhttp3.Interceptor
import okhttp3.Response

class GeoApiRequestInterceptor : Interceptor {


    //    private val userToken = UserManager.getInstance().getToken()
    var token: String = ""
    var context: Context? = null

    override fun intercept(chain: Interceptor.Chain?): Response {
        val original = chain!!.request()
        val requestBuilder = original.newBuilder()
        val url = original.url().toString()
        val userRegis = UserManager.getInstance()
//        val isForgot = DatabaseManager.getInstance().getisForgot().isForgotPin
        if (UserManager.getInstance().getProfile().token.isNotEmpty()) {
            token = DatabaseManager.getInstance().getUserInfo().token
        } else {
            token = FirebaseInstanceId.getInstance().token!!
        }

//        requestBuilder.addHeader("Authorization", "Bearer " +  userToken)
//        Log.e("isForgot  " , isForgot.toString())
        if (isApiRegisterFlow(url)) {
            requestBuilder.addHeader("Authorization", "Basic " + token)
        } else if (DatabaseManager.getInstance().getUserInfo().flagLogin) {
            requestBuilder.addHeader("Authorization", "Bearer " + token)

        } else {
            requestBuilder.addHeader("Authorization", "Basic " + token)
        }

//        if(isRegisterNonCustomer(url)){
//            requestBuilder.addHeader("Authorization", "Basic " + token)
//
//        }else if(isApiRegisterFlow(url)){
//            requestBuilder.addHeader("Authorization", "Basic " + token)
//        }else{
//            requestBuilder.addHeader("Authorization", "Bearer " + token)
//        }


        requestBuilder.addHeader("Content-Type", "application/json")
        return chain.proceed(requestBuilder.build())
    }

    private fun isRegisterNonCustomer(url: String): Boolean {
        return url == "${BuildConfig.BASE_URL}application/regisnoncust"
    }

    private fun isApiRegisterFlow(url: String): Boolean {
        return when (url) {
            "${BuildConfig.BASE_URL}application/Register" -> true
            "${BuildConfig.BASE_URL}application/SendOTP" -> true
            "${BuildConfig.BASE_URL}application/VerifyOTP" -> true
            "${BuildConfig.BASE_URL}application/BeforeRegister" -> true
            "${BuildConfig.BASE_URL}Application/SetPINRegis" -> true
            "${BuildConfig.BASE_URL}application/CheckMasterLoad" -> true
            "${BuildConfig.BASE_URL}application/UpdateMasterLoad" -> true
            else -> false
        }
    }

}