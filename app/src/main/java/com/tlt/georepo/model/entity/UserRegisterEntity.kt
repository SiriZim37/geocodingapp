package com.tlt.georepo.model2.entity

import com.tlt.georepo.common.extension.toCurrentByUTC
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class UserRegisterEntity (
    @PrimaryKey
    var id : String ="",
    var userID : String ="",
    var userPass : String ="",
    var flagLogin : Boolean = false,
    var fcmToken : String ="",
    var language : String =""
//        var jwtToken : String =""
) : RealmObject(){


    fun transformToRealm(user: UserRegisterEntity) {
        this@UserRegisterEntity.id = user.id
        this@UserRegisterEntity.userID = user.userID
        this@UserRegisterEntity.userPass = user.userPass
        this@UserRegisterEntity.flagLogin = user.flagLogin
        this@UserRegisterEntity.fcmToken = user.fcmToken
        this@UserRegisterEntity.language = user.language
//        this@UserRegisterEntity.jwtToken = user.jwtToken
    }

    fun transform() : UserRegisterEntity {
        return  UserRegisterEntity().apply {
            this.id = this@UserRegisterEntity.id
          this.userID =  this@UserRegisterEntity.userID
          this.userPass =  this@UserRegisterEntity.userPass
          this.flagLogin =  this@UserRegisterEntity.flagLogin
          this.fcmToken=  this@UserRegisterEntity.fcmToken
            this.language=  this@UserRegisterEntity.language
//            this.jwtToken = this@UserRegisterEntity.jwtToken
        }

    }



}

