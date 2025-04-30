package com.tlt.georepo.model2.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class UserPinCode (
        @PrimaryKey
        var userID : String ="",
        var pincode : String ="") : RealmObject(){

    fun transformToRealm(user: UserPinCode) {
        this@UserPinCode.userID = user.userID
        this@UserPinCode.pincode = user.pincode
    }

    fun transform() : UserPinCode {
        return  UserPinCode().apply {
            this.userID = this@UserPinCode.userID
            this.pincode =  this@UserPinCode.pincode
        }

    }

}

