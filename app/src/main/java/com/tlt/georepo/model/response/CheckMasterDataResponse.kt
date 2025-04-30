package com.tlt.georepo.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckMasterDataResponse(
        @SerializedName("FileName")
        @Expose
        val filename: String = "",
        @SerializedName("Data")
        @Expose
        val base64: String = ""
)