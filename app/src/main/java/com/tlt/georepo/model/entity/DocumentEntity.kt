package com.tlt.georepo.model.entity

import io.realm.RealmObject

open class DocumentEntity(
        var id: String = "",
        var base64: String = "",
        var type: String = "" ,
        var lat: String = "" ,
        var lng : String = ""
) : RealmObject()