package com.tlt.georepo.model.request

import com.google.gson.annotations.SerializedName

class SubmitJobRequest {
    @SerializedName("PKEY1")
    var pKEY1: String= ""
    @SerializedName("PKEY2")
    var pKEY2: String= ""
    @SerializedName("PKEY3")
    var pKEY3: String= ""
    @SerializedName("PKEY4")
    var pKEY4: String= ""
    @SerializedName("PKEY5")
    var pKEY5: String= ""
    @SerializedName("PKEY6")
    var pKEY6: String= ""
    @SerializedName("PKEY7")
    var pKEY7: String= ""
    @SerializedName("PSELECT")
    var pSELECT: String = ""
    @SerializedName("PLAT")
    var plat: String = ""
    @SerializedName("PLONG")
    var plng: String = ""

    companion object {
        fun build(
            actionCode: String,
            actionRemark: String,
            lat: String,
            lng: String,
            mainId: String,
            realAddress: String,
            realLat: String,
            realLng: String ,
            paymentDay : String
        ): SubmitJobRequest {
            return SubmitJobRequest().apply {
                pSELECT = actionCode
                pKEY1 = actionRemark
                pKEY2 = paymentDay ?: ""
                pKEY3 = ""
                pKEY4 = mainId
                pKEY5 = realAddress
                pKEY6 = realLat
                pKEY7 = realLng
                plat = lat
                plng = lng
            }
        }
    }
}
