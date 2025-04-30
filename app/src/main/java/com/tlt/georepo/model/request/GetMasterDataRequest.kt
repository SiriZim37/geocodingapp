package com.tlt.georepo.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetMasterDataRequest {

    @SerializedName("PSELECT")
    @Expose
    var pselect: String = ""
    @SerializedName("PSUCCESS")
    @Expose
    var psuccess: String = ""

    companion object {
        fun build(version : String = "") = GetMasterDataRequest().apply {
            pselect = version.replace(".bz2", "")
        }
    }
}