package com.tlt.georepo.model.request

import android.util.Log
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tlt.georepo.manager.db.DatabaseManager

class SetPINRegisRequest {

    @SerializedName("PSELECT")
    @Expose
    var pselect: String =""
    @SerializedName("PKEY1")
    @Expose
    var pkey1: String = ""
    @SerializedName("PKEY2")
    @Expose
    var pkey2: String = ""
    @SerializedName("PKEY3")
    @Expose
    var pkey3: String = ""
    @SerializedName("PLAT")
    @Expose
    var plat: String = ""
    @SerializedName("PLONG")
    @Expose
    var plng: String = ""

    companion object {
        fun buildForRegister(password: String): SetPINRegisRequest {
            return SetPINRegisRequest().apply {
                pselect = "INSERT"
                pkey1 = password
                pkey2  = ""
                pkey3 = ""
                plat = DatabaseManager.getInstance().getCurrent().latitude
                plng = DatabaseManager.getInstance().getCurrent().longitude
            }
        }

        fun buildForForgotPincode(password: String): SetPINRegisRequest {
            return SetPINRegisRequest().apply {
                pselect = "FORGOT"
                pkey1 = password
                pkey2  = ""
                pkey3 = ""
                plat = DatabaseManager.getInstance().getCurrent().latitude
                plng = DatabaseManager.getInstance().getCurrent().longitude
            }
        }
    }
}