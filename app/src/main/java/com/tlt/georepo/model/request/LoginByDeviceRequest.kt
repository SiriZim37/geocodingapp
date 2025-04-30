package com.tlt.georepo.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tlt.georepo.manager.db.DatabaseManager

class LoginByDeviceRequest {

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
    @SerializedName("PKEY4")
    @Expose
    var pkey4: String = ""
    @SerializedName("PKEY5")
    @Expose
    var pkey5: String = ""
    @SerializedName("PLAT")
    @Expose
    var plat: String = ""
    @SerializedName("PLONG")
    @Expose
    var plng: String = ""

    companion object {
        fun build(pincode: String , lat:String , lng : String): LoginByDeviceRequest {
            return LoginByDeviceRequest().apply {
                pselect = ""
                pkey1 = pincode
                pkey2  = "N"
                pkey3 = ""
                pkey4 = ""
                pkey5 = ""
                plat = DatabaseManager.getInstance().getCurrent().latitude
                plng = DatabaseManager.getInstance().getCurrent().longitude
            }
        }

    }
}

