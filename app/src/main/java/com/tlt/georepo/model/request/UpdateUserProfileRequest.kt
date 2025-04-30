package com.tlt.georepo.model.request

import android.os.Build
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jaredrummler.android.device.DeviceName
import com.tlt.georepo.BuildConfig


class UpdateUserProfileRequest {
    @SerializedName("PSELECT")
    @Expose
    var pSELECT: String? = ""
    @SerializedName("PKEY1")
    @Expose
    var pKEY1: String? = ""
    @SerializedName("PKEY2")
    @Expose
    var pKEY2: String? = ""
    @SerializedName("PKEY3")
    @Expose
    var pKEY3: String? = ""
    @SerializedName("PKEY4")
    @Expose
    var pKEY4: String? = ""
    @SerializedName("PKEY5")
    @Expose
    var pKEY5: String? = ""
    @SerializedName("PKEY6")
    @Expose
    var pKEY6: String? = ""
    @SerializedName("PKEY7")
    @Expose
    var pKEY7: String? = ""
    @SerializedName("PKEY8")
    @Expose
    var pKEY8: String? = ""


    companion object {
        fun build(lat: String, lng: String, email: String, phone: String, lang: String , offline:String): UpdateUserProfileRequest {
            return UpdateUserProfileRequest().apply {
                pSELECT = "UPDATE_PROFILE"
                pKEY1 = lat
                pKEY2 = lng
                pKEY3 = email
                pKEY4 = phone
                pKEY5 = ""
                pKEY6 = ""
                pKEY7 = lang
                pKEY8 = offline
            }
        }
    }
}