package com.tlt.georepo.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegistProfileRequest {

    @SerializedName("PKEY1")
    @Expose
    var pkeY1: String? = null
    @SerializedName("PKEY2")
    @Expose
    var pkeY2: String? = null
    @SerializedName("PKEY3")
    @Expose
    var pkeY3: String? = null

    companion object {
        fun build(lat:String , lng : String): RegistProfileRequest {
            return RegistProfileRequest().apply {
                pkeY1 = ""
                pkeY2 = lat
                pkeY3 = lng
            }
        }
    }

}