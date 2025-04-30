package com.tlt.georepo.model.entity


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserConfig(
    @PrimaryKey
    var id : String ="",
    var isInitial : Boolean = false,
    var isLoggedIn : Boolean = false,
    var isOverTime :Boolean = false) : RealmObject(){

    fun transformToRealm(user: UserConfig) {
        this@UserConfig.id = user.id
        this@UserConfig.isInitial = user.isInitial
        this@UserConfig.isLoggedIn = user.isLoggedIn
        this@UserConfig.isOverTime = user.isOverTime
    }

    fun transform() : UserConfig {
        return  UserConfig().apply {
            this.id = this@UserConfig.id
            this.isInitial =  this@UserConfig.isInitial
            this.isLoggedIn = this@UserConfig.isLoggedIn
            this.isOverTime = this@UserConfig.isOverTime
        }

    }

}
