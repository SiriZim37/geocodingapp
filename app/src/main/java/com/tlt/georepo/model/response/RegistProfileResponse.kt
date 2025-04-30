package com.tlt.georepo.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

 class RegistProfileResponse {

    @SerializedName("REGIS_ID")
    @Expose
    var regisid: String? = null
    @SerializedName("REGIS_NAME")
    @Expose
    var regisname: String? = null
    @SerializedName("REGIS_SURNAME")
    @Expose
    var regissurname: String? = null
    @SerializedName("PROFILE_IMG")
    @Expose
    var profileimg: String? = null
    @SerializedName("FLAG_APPROVE")
    @Expose
    var flagapprove: String? = null
    @SerializedName("APPROVE_DESC")
    @Expose
    var approvedesc: String? = null
    @SerializedName("MOBILE")
    @Expose
    var Mobile: String? = null
    @SerializedName("EMAIL")
    @Expose
    var Email: String? = null
    @SerializedName("START_WORKING_DATE")
    @Expose
    var startworkingdate: String? = null
    @SerializedName("COMPLETION_DATE")
    @Expose
    var completiondate: String? = null
    @SerializedName("CERT_IMG")
    @Expose
    var certimg: String? = null
    @SerializedName("OFFLINE_MODE")
    @Expose
    var offlinemode: String? = null
    @SerializedName("FLAG_LANGUAGE")
    @Expose
    var flaglanguage: String? = null
    @SerializedName("COMP_CODE")
    @Expose
    var compcode: String? = null
    @SerializedName("COMPANY_TH")
    @Expose
    var companyth: String? = null

}