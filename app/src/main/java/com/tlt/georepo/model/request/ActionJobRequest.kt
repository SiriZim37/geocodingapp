package com.tlt.georepo.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ActionJobRequest {
    @SerializedName("PKEY1")
    @Expose
    var pKEY1: String = ""
    @SerializedName("PKEY2")
    @Expose
    var pKEY2: String = ""
    @SerializedName("PKEY3")
    @Expose
    var pKEY3: String = ""
    @SerializedName("PKEY4")
    @Expose
    var pKEY4: String = ""
    @SerializedName("PKEY5")
    @Expose
    var pKEY5: String = ""
    @SerializedName("PSELECT")
    @Expose
    var pSELECT: String = ""
    @SerializedName("PLAT")
    @Expose
    var plat: String = ""
    @SerializedName("PLONG")
    @Expose
    var plong: String = ""

    companion object {
        fun build(type: String,
                  main_id : String ,
                  lat : String ,
                  lng : String ,
                  dateHold : String,
                  remark : String): ActionJobRequest {
            return ActionJobRequest().apply {
                pSELECT = type
                pKEY1 = main_id
                pKEY2 = ""
                pKEY3 = ""
                pKEY4= remark
                pKEY5= dateHold
                plat = lat
                plong = lng
            }
        }
    }
}