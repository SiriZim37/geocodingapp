package com.tlt.georepo.model.entity.masterdata

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class  Company : RealmObject() {
        @SerializedName("COMPANY_EN")
        var cOMPANYEN: String? =""
        @SerializedName("COMPANY_TH")
        var cOMPANYTH: String? =""
        @SerializedName("COMP_CODE")
        var cOMPCODE: String? =""
}