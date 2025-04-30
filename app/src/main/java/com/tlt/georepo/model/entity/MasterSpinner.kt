package com.tlt.georepo.model.entity
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MasterSpinner(
    @PrimaryKey
    var id : String ="",
    var title : String = ""): RealmObject(){

    fun transformToRealm(user: MasterSpinner) {
        this@MasterSpinner.id = user.id
        this@MasterSpinner.title = user.title
    }

    fun transform() : MasterSpinner {
        return  MasterSpinner().apply {
            this.id = this@MasterSpinner.id
            this.title =  this@MasterSpinner.title
        }

    }

}
