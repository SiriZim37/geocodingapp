package com.tlt.georepo.model.entity.cache

import io.realm.RealmModel

interface Jsonable : RealmModel {
    fun getJsonString() : String
    fun setJsonString(json : String)
}