package com.tlt.georepo.model.entity.masterdata

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class SubmitJob : RealmObject() {
        @SerializedName("MS_DESC_EN")
        var mSDESCEN: String? = ""
        @SerializedName("MS_DESC_TH")
        var mSDESCTH: String? = ""
        @SerializedName("MS_TYPE")
        var mSTYPE:  String? = ""
        @SerializedName("MS_VALUE")
        var mSVALUE: String? = ""
}