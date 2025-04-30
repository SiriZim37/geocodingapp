package com.tlt.georepo.model.response

import com.google.gson.annotations.SerializedName


data class GetJobAllResponse(
        @SerializedName("ALL") var rJOB: List<JOBALL?>? = listOf()
) {

    data class JOBALL(
            @SerializedName("JOB_NO") var rjobNo: String? = "",
            @SerializedName("CUST_NAME") var rcusName: String? = "",
            @SerializedName("CUST_ADDRESS") var rcusAddr: String? = "",
            @SerializedName("LATITUDE") var rlat: Double? ,
            @SerializedName("LONGTITUDE") var rlng: Double? ,
            @SerializedName("MOBILE_NO") var rmobileNo: String? = "",
            @SerializedName("REG_NO") var rregNo: String? = "",
            @SerializedName("CONTRACT_NO") var rcontractNo: String? = "",
            @SerializedName("UNPAID_AMT") var runpaidAmt: String? = "",
            @SerializedName("JOB_STATUS") var rjobStatus: String? = "",
            @SerializedName("DATETIME") var rdatetime: String? = ""
    )

}