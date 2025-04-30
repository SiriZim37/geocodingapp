package com.tlt.georepo.manager.db

import android.util.Log
import com.tlt.georepo.common.extension.toCurrentByUTC
import com.tlt.georepo.manager.security.SecureManager
import com.tlt.georepo.model.entity.UserInfo
import java.lang.Exception
import java.util.*

class UserManager() {

    fun getProfile() = DatabaseManager.getInstance().getUserInfo()

    fun saveProfile(user: UserInfo) {
        DatabaseManager.getInstance()
            .saveUserProfile(user)
    }

    fun uppdateProfile(user: UserInfo) {
        DatabaseManager.getInstance()
            .updateUserProfile(user)
    }


    fun saveAccessToken(accessToken: String?) {
        Log.e("saveAccessToken", accessToken)
        if (accessToken == null || accessToken.isEmpty()) {
            return
        }

        val user = getProfile().apply {
            this.userID = "1"
            this.token = accessToken
        }

        saveProfile(user)
        Log.e("saveAccessToken", UserManager.getInstance().getProfile().token)
    }
//
    fun isRegistered(): Boolean {
        var flagLogin = false
         try {
                flagLogin = DatabaseManager.getInstance()
                    .getUserInfo()?.flagLogin!!
             if(flagLogin){
                 var user = DatabaseManager.getInstance()
                     .getUserInfo()?.userID!!
                 if( user  == DatabaseManager.getInstance().GetPinCode()?.userID!! ) {
                     flagLogin = true
                 }else{
                     flagLogin = false
                 }

             }
        }catch (e : Exception){
                flagLogin = false
        }
        return  flagLogin
    }


    fun isRegisteredNonCust(): Boolean {
        var flagLogin = false
        try {
            if(UserManager.getInstance().getProfile().token.isNotEmpty()) {
                flagLogin = true
            }else{
               flagLogin = false
            }
        }catch (e : Exception){
            flagLogin = false
        }
        return  flagLogin
    }

//    fun isInitial(): Boolean {
//       var isInitial = SharedPref.getisInitialPref()
//    }

    fun flagCheckLogin(pin : String ): Boolean {
        var result = false
        try {
            if( pin  == DatabaseManager.getInstance().GetPinCode()?.pincode!! ) {
                result   = true
            }else{
                result = false
            }
        }catch (e : Exception){
            result = false
        }
        return  result
    }

    fun getUser() = DatabaseManager.getInstance().getUserInfo().userID

    fun hashPincodeForAuth(plaintext: String): String {
        val salt = getProfile().salt
        Log.e("setOnPincodeCompleteListener", salt + " plaintext "+ plaintext)
        val hash = SecureManager.getInstance().hash(plaintext, salt)
        Log.e("hash__", hash)
//        val saltForLogin = SecureManager.getInstance().generateSalt()
        return SecureManager.getInstance().hash(hash, salt)
//        return  salt
    }

    fun hashPincodeForSetup(plaintext: String): String {
        val salt = SecureManager.getInstance().generateSalt()
        val user = UserManager().getProfile().apply {
            this.salt = salt
        }
        UserManager().uppdateProfile(user)
        return SecureManager.getInstance().hash(plaintext,salt)
    }

    fun minusUnreadNotification() {
        val user = getProfile().apply {
            this.unreadNotification = this.unreadNotification - 1
        }
        saveProfile(user)
    }

    fun setZeroUnreadNotification() {
        val user = getProfile().apply {
            this.unreadNotification = 0
        }

        saveProfile(user)
    }

    fun getUnreadNotification() = getProfile().unreadNotification.toString()

    fun addUnreadNotification() {
        val user = getProfile().apply {
            this.unreadNotification = this.unreadNotification + 1
        }

        saveProfile(user)
    }

    fun updateLastActive() {
        val current = Date().toCurrentByUTC()
        val user = getProfile().apply {
            this.lastActive = current
        }

        DatabaseManager.getInstance().saveUserProfile(user)
    }

    fun isLastActiveOverLimit5mins(): Boolean {
        val current = Date().toCurrentByUTC()
        val lastActive = getProfile().lastActive
        val millseconds = current.time - lastActive.time
        val mins = millseconds / (1000 * 60) % 60

        return mins >= 5
    }

    companion  object {
        private val userManager = UserManager()
        fun getInstance() = userManager
    }


}