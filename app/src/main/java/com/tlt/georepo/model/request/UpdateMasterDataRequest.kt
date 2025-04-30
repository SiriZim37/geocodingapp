package com.tlt.georepo.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdateMasterDataRequest {

    @SerializedName("PSELECT")
    @Expose
    var pselect: String = ""
    @SerializedName("PSUCCESS")
    @Expose
    var psuccess: String = "Y"

    companion object {
        fun build(version: String) = UpdateMasterDataRequest().apply {
            pselect = version.replace(".bz2", "")
        }
    }
}