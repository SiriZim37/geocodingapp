package com.tlt.georepo.model.entity


import com.tlt.georepo.common.extension.toCurrentByUTC
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class UserInfo(
    var userID: String = "",
    var userName: String = "",
    var userEmail: String = "",
    var userPhone: String = "",
    var userIDCard: String = "",
    var userStartDate: String = "",
    var userCompany: String = "",
    var userBirthDate: String = "",
    var userCompletionDate: String = "" ,
    var token: String = "",
    var lastActive: Date = Date().toCurrentByUTC() ,
    var unreadNotification: Int = 0 ,
    var salt: String = "" ,
    var imgProfile: String = "",
    var flagLogin: Boolean = false
    , var flagPrivate: Boolean = false
    ) : RealmObject() {

    fun transformToRealm(user: UserInfo) {
        this@UserInfo.userID = user.userID
        this@UserInfo.userName = user.userName
        this@UserInfo.userEmail = user.userEmail
        this@UserInfo.userIDCard = user.userIDCard
        this@UserInfo.userPhone = user.userPhone
        this@UserInfo.userStartDate = user.userStartDate
        this@UserInfo.userCompany = user.userCompany
        this@UserInfo.userBirthDate = user.userBirthDate
        this@UserInfo.userCompletionDate = user.userCompletionDate
        this@UserInfo.token = user.token
        this@UserInfo.lastActive = user.lastActive
        this@UserInfo.salt = user.salt
        this@UserInfo.unreadNotification = user.unreadNotification
        this@UserInfo.imgProfile = user.imgProfile
        this@UserInfo.flagLogin = user.flagLogin
        this@UserInfo.flagPrivate = user.flagPrivate

    }

    fun transform(): UserInfo {
        return UserInfo().apply {
            this.userID = this@UserInfo.userID
            this.userName = this@UserInfo.userName
            this.userEmail = this@UserInfo.userEmail
            this.userIDCard = this@UserInfo.userIDCard
            this.userPhone = this@UserInfo.userPhone
            this.userStartDate = this@UserInfo.userStartDate
            this.userCompany = this@UserInfo.userCompany
            this.userBirthDate = this@UserInfo.userBirthDate
            this.userCompletionDate = this@UserInfo.userCompletionDate
            this.token = this@UserInfo.token
            this.salt = this@UserInfo.salt
            this.unreadNotification = this@UserInfo.unreadNotification
            this.lastActive = this@UserInfo.lastActive
            this.imgProfile = this@UserInfo.imgProfile
            this.flagLogin = this@UserInfo.flagLogin
            this.flagPrivate = this@UserInfo.flagPrivate
        }
    }
}
