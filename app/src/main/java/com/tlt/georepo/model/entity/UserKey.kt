package com.tlt.georepo.model.entity


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserKey(
    @PrimaryKey
    var userID : String ="",
    var userKey : String ="") : RealmObject(){

    fun transformToRealm(user: UserKey) {
        this@UserKey.userID = user.userID
        this@UserKey.userKey = user.userKey
    }

    fun transform() : UserKey {
        return  UserKey().apply {
            this.userID = this@UserKey.userID
            this.userKey =  this@UserKey.userKey
        }

    }

}
