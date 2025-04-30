package com.tlt.georepo.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tlt.georepo.util.CalendarUtils

class SubmitImageRequest {

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
    @SerializedName("PKEY6")
    @Expose
    var pkey6: String = ""
    @SerializedName("PKEY7")
    @Expose
    var pkey7: String = ""
    @SerializedName("PKEY8")
    @Expose
    var pkey8: String = ""
    @SerializedName("PKEY9")
    @Expose
    var pkey9: String = ""
    @SerializedName("PLAT")
    var plat: String = ""
    @SerializedName("PLONG")
    var plng: String = ""

    companion object {
        fun build(contractNumber: String): SubmitImageRequest {
            return SubmitImageRequest().apply {
                pkey1 = contractNumber
            }
        }

        fun buildForSendJob(
            lat :String ,
            lng :String ,
            mainID : String ,
            actionID : String ,
            nameImg :String ,
            imageBase64 : String ,
            imgLat : String,
            imgLng : String ,
            countImg : String
        ): SubmitImageRequest {
            return SubmitImageRequest().apply {
                pkey1 = ""
                pkey2 = ""
                pkey3 = mainID
                pkey4 = actionID
                pkey5 = nameImg // CalendarUtils.getCurrentDateByPattern("yyyyMMddHHmmss")
                pkey6 = imageBase64
                pkey7 = imgLat
                pkey8 = imgLng
                pkey9 = countImg
                plat = lat
                plng = lng
            }
        }
    }
}

