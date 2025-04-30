package com.tlt.georepo.model.request

import android.content.Context
import android.os.Build
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.jaredrummler.android.device.DeviceName
import com.tlt.georepo.BuildConfig

class NonCustomerRequest {
    private var mContext: Context? = null


    fun getRequestBody(): RegisterNonCustRequest {
        val request = RegisterNonCustRequest()
        request.pselect = "TH"
        request.pkeY1 = "CUST_ANDROID"
        request.pkeY2 = "${Build.MANUFACTURER} ${DeviceName.getDeviceName()}"
        request.pkeY3 = FirebaseInstanceId.getInstance().token!!
        request.pkeY4 = Build.MODEL
        request.pkeY5 = "Dev_test"
        request.pkeY6 = BuildConfig.VERSION_NAME
        request.pkeY7 =""
        request.pkeY8 =""
        request.puser = ""
        return request
    }

    fun RegisterNonCustomerRequest() {
        var platform = "CUST_ANDROID"
        var brand = "${Build.MANUFACTURER} ${DeviceName.getDeviceName()}"
        var fcmToken = FirebaseInstanceId.getInstance().token
        var osVersion = "Android ${Build.VERSION.RELEASE}"
        var appVersion = BuildConfig.VERSION_NAME
        var bildModel = Build.MODEL
        Log.e("platform", platform)
        Log.e("brand", brand)
        Log.e("branfcmTokend", fcmToken)
        Log.e("osVersion", osVersion)
        Log.e("appVersion", appVersion)
        Log.e("bildModel", bildModel)

    }

//    fun location(success: () -> Unit, failure: () -> Unit){
//        Log.e("location", "location")
//        GlobalScope.launch(Dispatchers.Main) {
//            try {
//                val location = LocationManager.getLastKnowLocation()
//                val latLng = LatLng(location.latitude, location.longitude)
//                LAT_CUR = location.latitude.toString()
//                LNG_CUR = location.longitude.toString()
//                val entity = CurrentLocationNonCust().apply {
//                    id = "1"
//                    latitude = LAT_CUR
//                    longitude  = LNG_CUR
//                }
//                DatabaseManager.getInstance().setCurrent(entity)
//                Log.e("latitude_Lex", DatabaseManager.getInstance().getCurrent().latitude)
//                Log.e("longitude_lex", DatabaseManager.getInstance().getCurrent().longitude)
//                var lat = DatabaseManager.getInstance().getCurrent().latitude
//                var long = DatabaseManager.getInstance().getCurrent().longitude
//                if(lat.isNotEmpty()&& long.isNotEmpty()){
//                        success.invoke()
//                    }else{
//                    failure.invoke()
//                }
//            } catch (error: Exception) {
//                Log.e("error", error.printStackTrace().toString())
//            }
//        }
//    }


    fun getRequestRegist(): RegistPost {
        val request = RegistPost()
//        request!!.pselect = ""
//        request!!.pkeY1 = "ทดสอบ"
////        request!!.pkeY2 = ed_firstname.text.toString() + " " + ed_lastname.text.toString()
//        request!!.pkeY3 = "email@test.com"
//        request!!.pkeY4 = "1234567891112"
////        request!!.pkeY5 = ed_phone.text.toString()
//        request!!.pkeY6 = strBirthdate
//        request!!.pkeY7 = "0001"
//        request!!.pkeY8 = strStartWorkingDate
//        request!!.pkeY9 = strCompletionDate
        return request
    }

    fun getReQuestMaster(): RegisterNonCustRequest {
        val request = RegisterNonCustRequest()
        request!!.pkeY1 = "01105000793"
        request!!.pkeY2 = ""
        request!!.pkeY3 = ""

        return request
    }

    fun getOTPRequestBody(otp: String): SendOTPPost {
        val request = SendOTPPost()
        request!!.pselect = "GEOCODING"
        request!!.pkeY1 = otp
        request!!.pkeY2 = ""
        request!!.pkeY3 = ""
        request!!.pkeY4 = ""
        request!!.pkeY5 = ""
        request!!.sign = ""
        return request

    }

    fun getPinRegistBody(pincode: String): SendOTPPost {
        val request = SendOTPPost()
        request!!.pselect = "INSERT"
        request!!.pkeY1 = pincode
        request!!.pkeY2 = ""
        request!!.pkeY3 = ""
        return request

    }


    fun getVerifyOTPRequestBody(otp: String): SendOTPPost {
        val request = SendOTPPost()
        request!!.pkeY1 = "REGISTER"
        request!!.pkeY2 = ""
        request!!.pkeY3 = ""
        request!!.pkeY4 = otp
        request!!.pkeY5 = ""
        request!!.pkeY6 = ""
        request!!.pkeY7 = ""
        return request

    }


    fun getLoginRequestBody(pincode: String , lat:String , lng : String ): SendOTPPost {
        val request = SendOTPPost()
        request!!.pselect = ""
        request!!.pkeY1 = pincode
        request!!.pkeY2 = "N"
        request!!.pkeY3 = lat
        request!!.pkeY4 = lng
        request!!.pkeY5 = ""
        return request

    }
}