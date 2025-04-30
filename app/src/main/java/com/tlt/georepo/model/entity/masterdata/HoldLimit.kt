package com.tlt.georepo.model.entity.masterdata

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class HoldLimit : RealmObject() {
        @SerializedName("HOLDLIMITDATE")
        var hOLDELIMITDATE: String? = ""
}