package com.tlt.georepo.model.response

import com.google.gson.annotations.SerializedName

data class ItemJobOnHandDetailResponse(
    @SerializedName("DETAIL")
    var dETAIL: DETAIL,
    @SerializedName("PROFILE")
    var pROFILE: PROFILE,
    @SerializedName("REMARK")
    var rEMARK: List<REMARK>
) {
    data class PROFILE(
        @SerializedName("ADDRESS_TYPE")
        var aDDRESSTYPE: String,
        @SerializedName("CUSTOMER_NAME")
        var cUSTOMERNAME: String,
        @SerializedName("GUARANTOR")
        var gUARANTOR: List<GUARANTOR>,
        @SerializedName("IDCARD")
        var iDCARD: String,
        @SerializedName("ID_PIC")
        var iDPIC: String,
        @SerializedName("LATITUDE")
        var lATITUDE: String,
        @SerializedName("LONGITUDE")
        var lONGITUDE: String,
        @SerializedName("PROFILE_ID")
        var pROFILEID: String,
        @SerializedName("REAL_ADDRESS")
        var rEALADDRESS: String,
        @SerializedName("RECORD_TYPE")
        var rECORDTYPE: String,
        @SerializedName("TEL_HOME")
        var tELHOME: String,
        @SerializedName("TEL_MOBILE")
        var tELMOBILE: String
    ) {
        data class GUARANTOR(
            @SerializedName("CUSTOMER_NAME")
            var cUSTOMERNAME: String,
            @SerializedName("IDCARD")
            var iDCARD: String,
            @SerializedName("ID_PIC")
            var iDPIC: String,
            @SerializedName("ADDRESS_ID")
            var aDDRESS_ID: String,
            @SerializedName("RECORD_TYPE")
            var rECORDTYPE: String,
            @SerializedName("TEL_HOME")
            var tELHOME: String,
            @SerializedName("TEL_MOBILE")
            var tELMOBILE: String
        )
    }

    data class REMARK(
        @SerializedName("JOB_REMARK")
        var jOBREMARK: String,
        @SerializedName("JOB_REMARK_DATE")
        var jOBREMARKDATE: String
    )

    data class DETAIL(
        @SerializedName("ADDRESS_ID")
        var aDDRESSID: String,
        @SerializedName("BRANCH")
        var bRANCH: String,
        @SerializedName("CHASIS_NO")
        var cHASISNO: String,
        @SerializedName("COLOR_DESC")
        var cOLORDESC: String,
        @SerializedName("CONTRACT_NO")
        var cONTRACTNO: String,
        @SerializedName("CONTRACT_STATUS")
        var cONTRACTSTATUS: String,
        @SerializedName("ENGINE_NO")
        var eNGINENO: String,
        @SerializedName("EXPECT_SUING_DATE")
        var eXPECTSUINGDATE: String,
        @SerializedName("INSTALLMENT_AMT")
        var iNSTALLMENTAMT: String,
        @SerializedName("INSTALLMENT_DUE_AMT")
        var iNSTALLMENTDUEAMT: String,
        @SerializedName("JOB_ID")
        var jOBID: String,
        @SerializedName("LAST_DUE_DATE")
        var lASTDUEDATE: String,
        @SerializedName("MAIN_ID")
        var mAINID: String,
        @SerializedName("MODEL_DESC")
        var mODELDESC: String,
        @SerializedName("OTHERS_AMT")
        var oTHERSAMT: String,
        @SerializedName("OVERDUE_DATE_FROM")
        var oVERDUEDATEFROM: String,
        @SerializedName("OVERDUE_DATE_TO")
        var oVERDUEDATETO: String,
        @SerializedName("OVERDUE_DAYS")
        var oVERDUEDAYS: String,
        @SerializedName("OVERDUE_ITEM")
        var oVERDUEITEM: String,
        @SerializedName("OVERDUE_ITEM_FROM")
        var oVERDUEITEMFROM: String,
        @SerializedName("OVERDUE_ITEM_TO")
        var oVERDUEITEMTO: String,
        @SerializedName("PAID_DATE")
        var pAIDDATE: String,
        @SerializedName("PAID_TO_ITEM")
        var pAIDTOITEM: String,
        @SerializedName("PENALTY_AMT")
        var pENALTYAMT: String,
        @SerializedName("PENALTY_DATE")
        var pENALTYDATE: String,
        @SerializedName("REGISTER_NO")
        var rEGISTERNO: String,
        @SerializedName("REGISTER_PROVINCE")
        var rEGISTERPROVINCE: String,
        @SerializedName("TOTAL_OVD_AMT")
        var tOTALOVDAMT: String
    )
}