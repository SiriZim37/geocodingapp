package com.tlt.georepo.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class JobCollectionDetailRequest {
    @SerializedName("PKEY1")
    @Expose
    var pKEY1: String = ""
    @SerializedName("PKEY2")
    @Expose
    var pKEY2: String = ""
    @SerializedName("PKEY3")
    @Expose
    var pKEY3: String = ""
    @SerializedName("PLAT")
    @Expose
    var plat: String = ""
    @SerializedName("PLONG")
    @Expose
    var plng: String = ""

    companion object {
        fun build(res : String , lat : String , lng : String ): JobCollectionDetailRequest {
            return JobCollectionDetailRequest().apply {
                pKEY1 = res
                pKEY2 = ""
                pKEY3 = ""
                plat = lat
                plng = lng
            }
        }
    }
}
