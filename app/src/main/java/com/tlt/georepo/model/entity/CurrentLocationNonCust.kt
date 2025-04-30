package com.tlt.georepo.model.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CurrentLocationNonCust(
    @PrimaryKey
    var id : String ="",
    var latitude : String = "",
    var longitude : String = "") : RealmObject(){

    fun transformToRealm(location: CurrentLocationNonCust) {
        this@CurrentLocationNonCust.id = location.id
        this@CurrentLocationNonCust.latitude = location.latitude
        this@CurrentLocationNonCust.longitude = location.longitude
    }

    fun transform() : CurrentLocationNonCust {
        return  CurrentLocationNonCust().apply {
            this.id = this@CurrentLocationNonCust.id
            this.latitude =  this@CurrentLocationNonCust.latitude
            this.longitude = this@CurrentLocationNonCust.longitude
        }

    }

}