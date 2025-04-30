package com.tlt.georepo.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetDataDirectionRequest{
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
    @SerializedName("PLAT")
    @Expose
    var pLAT: String = ""
    @SerializedName("PLONG")
    @Expose
    var pLONG: String = ""

    companion object {
        fun build( lat:String , lng : String , desLat: String  , desLng: String  ): GetDataDirectionRequest {
            return GetDataDirectionRequest().apply {
                pKEY1 = ""
                pKEY2  = ""
                pKEY3 =lat
                pKEY4 = lng
                pLAT = desLat
                pLONG = desLng
            }
        }

    }
}
