package com.tlt.georepo.model.entity.masterdata

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class Termscon  : RealmObject() {
    @SerializedName("MASTER_ID")
    var mASTERID:String? =""
    @SerializedName("REC_ACTIVE")
    var rECACTIVE:String? =""
    @SerializedName("TERM_FUNC1")
    var tERMFUNC1:String? =""
    @SerializedName("TERM_FUNC2")
    var tERMFUNC2: String? =""
    @SerializedName("TERM_FUNC3")
    var tERMFUNC3: String? =""
    @SerializedName("TERM_FUNC4")
    var tERMFUNC4:String? =""
    @SerializedName("TERM_INFO_EN")
    var tERMINFOEN:String? =""
    @SerializedName("TERM_INFO_TH")
    var tERMINFOTH:String? =""
    @SerializedName("TYPE_SYNC")
    var tYPESYNC: String? =""
}