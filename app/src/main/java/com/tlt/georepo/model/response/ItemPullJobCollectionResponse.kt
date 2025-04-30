package com.tlt.georepo.model.response

import com.google.gson.annotations.SerializedName

data class ItemPullJobCollectionResponse(
    @SerializedName("ADDRESS_ID") var aDDRESSID: String,
    @SerializedName("BRANCH") var bRANCH: String,
    @SerializedName("CONTRACT_NO") var cONTRACTNO: String,
    @SerializedName("CUSTOMER_NAME") var cUSTOMERNAME: String,
    @SerializedName("IDCARD") var iDCARD: String,
    @SerializedName("INSTALLMENT_DUE_AMT") var iNSTALLMENTDUEAMT: String,
    @SerializedName("OVERDUE_ITEM") var oVERDUE_ITEM: String,
    @SerializedName("OVERDUE_DAYS") var oVERDUE_DAYS: String,
    @SerializedName("COLOR_DESC") var cOLOR_DESC: String,
    @SerializedName("MODEL_DESC") var mODEL_DESC: String,
    @SerializedName("RECORD_TYPE") var rECORD_TYPE: String,
    @SerializedName("JOB_ID") var jOBID: String,
    @SerializedName("LATITUDE") var lATITUDE: String,
    @SerializedName("LONGITUDE") var lONGITUDE: String,
    @SerializedName("MAIN_ID") var mAINID: String,
    @SerializedName("PROFILE_ID") var pROFILEID: String,
    @SerializedName("REAL_ADDRESS") var rEALADDRESS: String,
    @SerializedName("REGISTER_NO") var rEGISTERNO: String,
    @SerializedName("REGISTER_PROVINCE") var rEGISTERPROVINCE: String ,
    @SerializedName("DISTANCT_KM") var dISTANCT_KM: String
)
