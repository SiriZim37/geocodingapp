package com.tlt.georepo.model.entity.masterdata

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class Disclosure : RealmObject() {
    @SerializedName("REC_ACTIVE")
    var rECACTIVE:  String? = ""
     @SerializedName("TC_CODE")
     var tCCODE:String? = ""
    @SerializedName("TERM_FUNC1")
    var tERMFUNC1: String? = ""
    @SerializedName("TERM_FUNC2")
    var tERMFUNC2:  String? = ""
    @SerializedName("TERM_FUNC3")
    var tERMFUNC3:  String? = ""
    @SerializedName("TERM_FUNC4")
    var tERMFUNC4: String? = ""
    @SerializedName("TERM_INFO_EN")
    var tERMINFOEN: String? = ""
    @SerializedName("TERM_INFO_TH")
    var tERMINFOTH: String? = ""
    @SerializedName("TYPE_SYNC")
    var tYPESYNC:  String? = ""
}