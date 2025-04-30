package com.tlt.georepo.model.request

import android.os.Build
import android.provider.Settings.Global.getString
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jaredrummler.android.device.DeviceName
import com.tlt.georepo.BuildConfig
import com.tlt.georepo.R

class RegisterNonCustRequest {
    @SerializedName("PSELECT")
    @Expose
    var pselect: String = ""
    @SerializedName("PKEY1")
    @Expose
    var pkeY1: String = ""
    @SerializedName("PKEY2")
    @Expose
    var pkeY2: String = ""
    @SerializedName("PKEY3")
    @Expose
    var pkeY3: String = ""
    @SerializedName("PKEY4")
    @Expose
    var pkeY4: String = ""
    @SerializedName("PKEY5")
    @Expose
    var pkeY5: String = ""
    @SerializedName("PKEY6")
    @Expose
    var pkeY6: String = ""
    @SerializedName("PKEY7")
    @Expose
    var pkeY7: String = ""
    @SerializedName("PKEY8")
    @Expose
    var pkeY8: String = ""
    @SerializedName("PUSER")
    @Expose
    var puser: String = ""

    companion object {
        fun build( lat:String  , lng: String): RegisterNonCustRequest {
            return RegisterNonCustRequest().apply {
                pselect =  "TH"
                pkeY1 =  "CUST_ANDROID"
                pkeY2 =  "${Build.MANUFACTURER} ${DeviceName.getDeviceName()}"
                pkeY3 =  FirebaseInstanceId.getInstance().token!!
                pkeY4 =  Build.MODEL + " ( SDK : " +(Build.VERSION.SDK_INT) + ")"
                pkeY5 =  "GeoCoding"
                pkeY6 =  BuildConfig.VERSION_NAME
                pkeY7 = lat
                pkeY8 = lng
                puser = ""
            }
        }
    }

}
