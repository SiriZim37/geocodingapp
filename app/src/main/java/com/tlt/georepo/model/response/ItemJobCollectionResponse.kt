package com.tlt.georepo.model.response

import com.google.gson.annotations.SerializedName

data class ItemJobCollectionResponse(
    @SerializedName("ACTION_CODE")
    var aCTIONCODE: String,
    @SerializedName("ACTION_REMARK")
    var aCTIONREMARK: String,
    @SerializedName("ACTION_DATE")
    var aCTIONDATE: String,
    @SerializedName("ADDRESS_ID")
    var aDDRESSID: String,
    @SerializedName("BRANCH")
    var bRANCH: String,
    @SerializedName("CONTRACT_NO")
    var cONTRACTNO: String,
    @SerializedName("CUSTOMER_NAME")
    var cUSTOMERNAME: String,
    @SerializedName("IDCARD")
    var iDCARD: String,
    @SerializedName("INSTALLMENT_DUE_AMT")
    var iNSTALLMENTDUEAMT: String,
    @SerializedName("JOB_ID")
    var jOBID: String,
    @SerializedName("LATITUDE")
    var lATITUDE: String,
    @SerializedName("LONGITUDE")
    var lONGITUDE: String,
    @SerializedName("MAIN_ID")
    var mAINID: String,
    @SerializedName("PROFILE_ID")
    var pROFILEID: String,
    @SerializedName("REAL_ADDRESS")
    var rEALADDRESS: String,
    @SerializedName("REGISTER_NO")
    var rEGISTERNO: String,
    @SerializedName("REGISTER_PROVINCE")
    var rEGISTERPROVINCE: String ,
    @SerializedName("RECORD_TYPE")
    var rECORD_TYPE: String,
    @SerializedName("OVERDUE_ITEM")
    var oVERDUE_ITEM: String ,
    @SerializedName("DISTANCT_KM")
    var dISTANCT_KM: String

)