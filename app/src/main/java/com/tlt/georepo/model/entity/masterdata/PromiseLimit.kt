package com.tlt.georepo.model.entity.masterdata

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class PromiseLimit : RealmObject() {
        @SerializedName("PROMISELIMITDATE")
        var pROMISELIMITDATE: String? = ""
}