package com.tlt.georepo.manager.db

import android.content.Context
import android.util.Log
import com.tlt.georepo.BuildConfig
import com.tlt.georepo.manager.ContextManager
import com.tlt.georepo.manager.FlowManager
import com.tlt.georepo.manager.JsonMapperManager
import com.tlt.georepo.manager.security.SecureManager
import com.tlt.georepo.model.entity.*
import com.tlt.georepo.model.entity.cache.Jsonable
import com.tlt.georepo.model.entity.masterdata.*
import com.tlt.georepo.model.request.RegistPost
import com.tlt.georepo.model.response.WSMsg
import com.tlt.georepo.model2.entity.UserPinCode
import com.tlt.georepo.model2.entity.UserRegisterEntity
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject
import io.realm.Sort
import java.lang.Exception
import javax.crypto.BadPaddingException

class DatabaseManager() {

    private val LOGIN_ATTEMPT_PREF = "LOGIN_ATTEMPT"
    private val MASTER_DATA_VERSION = "MASTER_DATA_VERSION"
    private val APP_STATE = "APP_STATE"
    private val SOCIAL_LOGIN_STATE = "SOCIAL_LOGIN_STATE"
    private val NOTIFY_DEEPLINK_STATE = "NOTIFY_DEEPLINK_STATE"
    private val FLOW_STATE = "FLOW_STATE"
    private val PAYMENT_FLOW_STATE = "FLOW_STATE"
    private val SECRET_MODE = "SECRET_MODE"

    // config value
    private lateinit var realmConfiguration: RealmConfiguration
    private val DB_NAME = "REPO.realm"
    private fun initDatabase() {
        realmConfiguration = getConfig()!!

    }

    fun saveUserProfile(user: UserInfo) {
        var realm = Realm.getInstance(realmConfiguration)
        try {
            realm.beginTransaction()
            realm.delete(UserInfo::class.java)
            realm.copyToRealm(user)
            realm.commitTransaction()
            realm.close()
        } catch (e: Exception) {
            realm.close()
        }
    }

    fun deleteUserInfo(){
        val realm = Realm.getInstance(realmConfiguration)
        try {
            realm.beginTransaction()
            realm.delete(UserInfo::class.java)
            realm.commitTransaction()
            realm.close()
        } catch (e: Exception) {
            realm.close()
        }
    }

    fun updateUserProfile(userReceive: UserInfo) {
        Log.e("uodateUserProfile", userReceive.salt)
        var realm = Realm.getInstance(realmConfiguration)
        try {
            var user = realm.where(UserInfo::class.java)
              .equalTo("userID", "1").findFirst()
            realm.beginTransaction()
            user!!.salt = userReceive.salt
            realm.commitTransaction()
            realm.close()
        } catch (e: Exception) {
            realm.close()
        }
    }


    fun updateImgProfile(userReceive: UserInfo) {
        Log.e("uodateIMGProfile", userReceive.imgProfile)
        var realm = Realm.getInstance(realmConfiguration)
        try {
            var user = realm.where(UserInfo::class.java)
                .equalTo("userID", "1").findFirst()
            realm.beginTransaction()
            user!!.imgProfile = userReceive.imgProfile
            realm.commitTransaction()
            realm.close()
        } catch (e: Exception) {
            realm.close()
        }
    }

    fun updateFlaglogin(userReceive: UserInfo) {
        Log.e("uodateUserProfile", userReceive.salt)
        var realm = Realm.getInstance(realmConfiguration)
        try {
            var user = realm.where(UserInfo::class.java)
                .equalTo("userID", "1").findFirst()
            realm.beginTransaction()
            user!!.flagLogin = userReceive.flagLogin
            realm.commitTransaction()
            realm.close()
        } catch (e: Exception) {
            realm.close()
        }
    }

    fun updateFlagPrivate(userReceive: UserInfo) {
        var realm = Realm.getInstance(realmConfiguration)
        try {
            var user = realm.where(UserInfo::class.java)
                .equalTo("userID", "1").findFirst()
            realm.beginTransaction()
            user!!.flagPrivate = userReceive.flagPrivate
            realm.commitTransaction()
            realm.close()
        } catch (e: Exception) {
            realm.close()
        }
    }


    fun saveNewUser(items: UserRegisterEntity) {
        var realm = Realm.getInstance(realmConfiguration)
        try {
            realm.beginTransaction()
            realm.copyToRealm(items)
            realm.commitTransaction()
            realm.close()
        } catch (e: Exception) {
            realm.close()
        }
    }

    fun saveUserKey(items: UserKey) {
        val realm = Realm.getInstance(realmConfiguration)
        try {
            realm.beginTransaction()
            realm.delete(UserKey::class.java)
            realm.copyToRealm(items)
            realm.commitTransaction()
            realm.close()
        } catch (e: Exception) {
            realm.close()
        }
    }
//    fun saveisForgot(isForgot: Boolean) {
//        val items = ForgotPincode().apply {
//            userID = "1"
//            isForgotPin = isForgot
//        }
//        val realm = Realm.getInstance(realmConfiguration)
//        try {
//            realm.beginTransaction()
//            realm.delete(ForgotPincode::class.java)
//            realm.copyToRealm(items)
//            realm.commitTransaction()
//            realm.close()
//        } catch (e: Exception) {
//            realm.close()
//        }
//    }


//    fun getisForgot(): ForgotPincode {
//        val realm = Realm.getInstance(realmConfiguration)
//        try {
//            var entity = realm.where(ForgotPincode::class.java).findFirst()
//            if (entity == null) {
//                realm.beginTransaction()
//                entity = realm.createObject(ForgotPincode::class.java)
//                realm.commitTransaction()
//            }
//            val result = entity!!.transform()
//            realm.close()
//            return result
//        } catch (e: Exception) {
//            realm.close()
//            return ForgotPincode()
//        }
//    }



    fun deleteNotifyByKey(key: String) {
        val realm = Realm.getInstance(realmConfiguration)

        realm.beginTransaction()

        realm.where(NotifyEntity::class.java)
            .equalTo("notifyKey", key)
            .findAll()
            .deleteAllFromRealm()

        realm.commitTransaction()

        realm.close()
    }

    fun saveUserKeyToRealm(results: WSMsg){
        val entity = UserKey().apply {
            userID = "1"
            userKey = results.getRslc()!!
        }
        DatabaseManager.getInstance().saveUserKey(entity)
        Log.e("Key_User_apiSendOTP", DatabaseManager.getInstance().getUserKey()?.userKey!!)
    }


    fun clearMasterSpinner() {
        val realm = Realm.getInstance(realmConfiguration)
        try {
            realm.beginTransaction()
            realm.delete(MasterSpinner::class.java)
            realm.commitTransaction()
            realm.close()
        } catch (e: Exception) {
            realm.close()
        }
    }

    fun saveMasterSpinner(items: MasterSpinner) {
        val realm = Realm.getInstance(realmConfiguration)
        try {
            realm.beginTransaction()
            realm.insertOrUpdate(items)
            realm.commitTransaction()
            realm.close()
        } catch (e: Exception) {
            realm.close()
        }
    }


    fun saveUserInfo(items: UserInfo) {
        val realm = Realm.getInstance(realmConfiguration)
        try {
            realm.beginTransaction()
            realm.delete(UserInfo::class.java)
            realm.insertOrUpdate(items)
            realm.commitTransaction()
            realm.close()
        } catch (e: Exception) {
            realm.close()
        }
    }


//    fun saveDataRegistToRealm(
//        results: WSMsg,
//        requestBody: RegistPost
//    ) {
//        val entity = UserKey().apply {
//            userID = "1"
//            userKey = results.getRslc()!!
//        }
//        DatabaseManager.getInstance().saveUserKey(entity)
//        Log.e("Key_User_apiRegister", DatabaseManager.getInstance().getUserKey()?.userKey!!)
//
//        val info = UserInfo().apply {
//            userID = "1"
//            userName = requestBody.pkeY2.toString()
//            userEmail = requestBody.pkeY3.toString()
//            userIDCard = requestBody.pkeY4.toString()
//            userPhone = requestBody.pkeY5.toString()
//            userBirthDate = requestBody.pkeY6.toString()
//            userCompany = requestBody.pkeY7.toString()
//            userStartDate = requestBody.pkeY8.toString()
//            userCompletionDate = requestBody.pkeY9.toString()
//        }
//        DatabaseManager.getInstance().saveUserInfo(info)
//    }

    fun getUserInfo(): UserInfo {
        val realm = Realm.getInstance(realmConfiguration)
        try {
            var entity = realm.where(UserInfo::class.java).findFirst()
            if (entity == null) {
                realm.beginTransaction()
                entity = realm.createObject(UserInfo::class.java)
                realm.commitTransaction()
            }
            val result = entity!!.transform()
            realm.close()
            return result
        } catch (e: Exception) {
            realm.close()
            return UserInfo()
        }
    }



//    fun saveMasterSpinner(items: MasterSpinner) {
//        val realm = Realm.getInstance(realmConfiguration)
//        realm.executeTransactionAsync {
//            val item = MasterSpinner()
//            item.title = items.title
//            it.insert(item)
//        }
//    }



//    fun getMasterSpinner(): MasterSpinner {
//        val realm = Realm.getInstance(realmConfiguration)
//        try {
//            var entity = realm.where(MasterSpinner::class.java).findFirst()
//            if (entity == null) {
//                realm.beginTransaction()
//                entity = realm.createObject(MasterSpinner::class.java)
//                realm.commitTransaction()
//            }
//            val result = entity!!.transform()
//            realm.close()
//            return result
//        } catch (e: Exception) {
//            realm.close()
//            return MasterSpinner()
//        }
//
//    }

    fun getMasterSpinner(): List<Company>? {
        val realm = Realm.getInstance(realmConfiguration)
        val entities = realm.where(Company::class.java).findAll()
        val result = realm.copyFromRealm(entities)
        realm.close()
        return result
    }
//    +


    fun GetUserRegister(): UserRegisterEntity {
        val realm = Realm.getInstance(realmConfiguration)
        try {
            var entity = realm.where(UserRegisterEntity::class.java).findFirst()
            if (entity == null) {
                realm.beginTransaction()
                entity = realm.createObject(UserRegisterEntity::class.java)
                realm.commitTransaction()
            }
            val result = entity!!.transform()
            realm.close()
            return result
        } catch (e: Exception) {
            realm.close()
            return UserRegisterEntity()
        }

//        var realm = Realm.getInstance(realmConfiguration)
//        realm.beginTransaction()
//        realm.copyToRealm(items)
//        realm.commitTransaction()
//        realm.close()
    }

    fun saveUserPincode(items: UserPinCode) {
        var realm = Realm.getInstance(realmConfiguration)
        try {
            realm.beginTransaction()
            realm.delete(UserPinCode::class.java)
            realm.copyToRealm(items)
            realm.commitTransaction()
            realm.close()
        } catch (e: Exception) {
            realm.close()
        }
    }

    fun GetPinCode(): UserPinCode {
        val realm = Realm.getInstance(realmConfiguration)
        try {
            var entity = realm.where(UserPinCode::class.java).findFirst()
            if (entity == null) {
                realm.beginTransaction()
                entity = realm.createObject(UserPinCode::class.java)
                realm.commitTransaction()
            }
            val result = entity!!.transform()
            realm.close()
            return result
        } catch (e: Exception) {
            realm.close()
            return UserPinCode()
        }

    }

    fun getUserKey(): UserKey {
        val realm = Realm.getInstance(realmConfiguration)
        try {
            var entity = realm.where(UserKey::class.java).findFirst()
            if (entity == null) {
                realm.beginTransaction()
                entity = realm.createObject(UserKey::class.java)
                realm.commitTransaction()
            }
            val result = entity!!.transform()
            realm.close()
            return result
        } catch (e: Exception) {
            realm.close()
            return UserKey()
        }

    }

    fun getOvertime(): UserConfig {
        val realm = Realm.getInstance(realmConfiguration)
        try {
            var entity = realm.where(UserConfig::class.java).findFirst()
            if (entity == null) {
                realm.beginTransaction()
                entity = realm.createObject(UserConfig::class.java)
                realm.commitTransaction()
            }
            val result = entity!!.transform()
            realm.close()
            return result
        } catch (e: Exception) {
            realm.close()
            return UserConfig()
        }

    }



    fun getCurrent(): CurrentLocationNonCust {
        val realm = Realm.getInstance(realmConfiguration)
        try {
            var entity = realm.where(CurrentLocationNonCust()::class.java).findFirst()
            if (entity == null) {
                realm.beginTransaction()
                entity = realm.createObject(CurrentLocationNonCust::class.java)
                realm.commitTransaction()
            }
            val result = entity!!.transform()
            realm.close()
            return result
        } catch (e: Exception) {
            realm.close()
            return CurrentLocationNonCust()
        }
    }

    fun getUserToken(): UserInfo {
        val realm = Realm.getInstance(realmConfiguration)
        try {
            var entity = realm.where(UserInfo::class.java).findFirst()
            if (entity == null) {
                realm.beginTransaction()
                entity = realm.createObject(UserInfo::class.java)
                realm.commitTransaction()
            }
            val result = entity!!.transform()
            realm.close()
            return result
        } catch (e: Exception) {
            realm.close()
            return UserInfo()
        }

    }




    fun <T : RealmObject> save(realmClass: Class<T>, list: List<T>) {
        val realm = Realm.getInstance(realmConfiguration)
        realm.beginTransaction()
        realm.copyToRealm(list)
        realm.commitTransaction()
        realm.close()
    }

    fun <T : RealmObject> deleteBy(realmClass: Class<T>, key: String = "", value: String = "") {
        val realm = Realm.getInstance(realmConfiguration)
        val query = if (key.isEmpty() || value.isEmpty()) {
            realm.where(realmClass)
        } else {
            realm.where(realmClass).equalTo(key, value)
        }

        val items = query.findAll()

        if (items.isEmpty()) {
            realm.close()
            return
        }

        realm.beginTransaction()
        items.deleteAllFromRealm()
        realm.commitTransaction()
        realm.close()
    }

    fun <T : RealmObject> findAllBy(realmClass: Class<T>): List<T>? {
        val realm = Realm.getInstance(realmConfiguration)
        val entities = realm.where(realmClass).findAll()
        val result = realm.copyFromRealm(entities)
        realm.close()
        return result
    }

    fun <T : RealmObject> findAllBy(realmClass: Class<T>, key: String = "", value: String = ""): List<T>? {
        val realm = Realm.getInstance(realmConfiguration)
        val query = if (key.isEmpty() || value.isEmpty()) {
            realm.where(realmClass)
        } else {
            realm.where(realmClass).equalTo(key, value)
        }

        val entities = query.findAll()
        val result = realm.copyFromRealm(entities)
        realm.close()
        return result
    }

    fun <T : Jsonable> saveCache(cacheRealmClass: Class<T>, model: Any) {
        val realm = Realm.getInstance(realmConfiguration)
        realm.beginTransaction()
        realm.where(cacheRealmClass).findAll().deleteAllFromRealm()
        realm.createObject(cacheRealmClass).apply {
            setJsonString(JsonMapperManager.getInstance().gson.toJson(model))
        }
        realm.commitTransaction()
        realm.close()
    }

    fun <T : Jsonable, E> findCacheBy(cacheRealmClass: Class<T>, model: Class<E>): E? {
        val realm = Realm.getInstance(realmConfiguration)
        val entities = realm.where(cacheRealmClass).findAll()
        val result = realm.copyFromRealm(entities)
        val cache = JsonMapperManager.getInstance()
            .gson
            .fromJson(result.firstOrNull()?.getJsonString(), model)
        realm.close()
        return cache
    }

    private fun getConfig(): RealmConfiguration {
        val realmConfig = RealmConfiguration.Builder()
            .name(DB_NAME)
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(1)
        return realmConfig.build()

    }

    private fun getRealmConfig(): RealmConfiguration? {
        val appKey = try {
            SecureManager.getInstance().getAppKey()
        } catch (badPaddingException: BadPaddingException) {
            clear()
            SecureManager.getInstance().getAppKey()
        }

        val realmConfig = RealmConfiguration.Builder()
            .name(DB_NAME)
            .schemaVersion(1)

        if (!BuildConfig.DEBUG) {
            realmConfig.encryptionKey(appKey)
        }

        return realmConfig.build()
    }


    //// ADD FROM MASTER ////

//    fun getProvinceList(): List<MailingA>? {
//        val realm = Realm.getInstance(realmConfiguration)
//        val entities = realm.where(MailingA::class.java)
//            .distinct(MailingA.PROVINCE_CODE)
//            .findAll()
//        val result = realm.copyFromRealm(entities)
//        realm.close()
//        return result
//    }
//
//    fun getAmphurListByProvinceCode(provinceCode: String): List<MailingA>? {
//        val realm = Realm.getInstance(realmConfiguration)
//        val entities = realm.where(MailingA::class.java)
//            .equalTo(MailingA.PROVINCE_CODE, provinceCode)
//            .findAll()
//        val result = realm.copyFromRealm(entities)
//        realm.close()
//        return result
//    }


    fun getMasterDataVersion(): String {
        // biw
        val context = ContextManager.getInstance().getApplicationContext()

        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            .getString(MASTER_DATA_VERSION, "")
    }

    fun saveMasterDataVersion(version: String = "") {
        val context = ContextManager.getInstance().getApplicationContext()
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            .edit()
            .putString(MASTER_DATA_VERSION, version)
            .commit()
    }

    fun isNotifyDeeplinkState(): Boolean {
        val context = ContextManager.getInstance().getApplicationContext()

        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            .getBoolean(NOTIFY_DEEPLINK_STATE, false)
    }

    fun updateNotifyDeeplinkState(isNotifyDeeplinkState: Boolean = false) {
        val context = ContextManager.getInstance().getApplicationContext()

        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(NOTIFY_DEEPLINK_STATE, isNotifyDeeplinkState)
            .commit()
    }

    fun isSocialLoginState(): Boolean {
        val context = ContextManager.getInstance().getApplicationContext()

        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            .getBoolean(SOCIAL_LOGIN_STATE, false)
    }

    fun updateSocialLoginState(isSocialLoginState: Boolean = false) {
        val context = ContextManager.getInstance().getApplicationContext()

        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(SOCIAL_LOGIN_STATE, isSocialLoginState)
            .commit()
    }

    fun isAppStateForeground(): Boolean {
        val context = ContextManager.getInstance().getApplicationContext()

        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            .getBoolean(APP_STATE, false)
    }

    fun updateAppState(isAppStateForeground: Boolean = false) {
        val context = ContextManager.getInstance().getApplicationContext()

        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(APP_STATE, isAppStateForeground)
            .commit()
    }

    fun getFlowState(): String {
        val context = ContextManager.getInstance().getApplicationContext()

        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            .getString(FLOW_STATE, FlowManager.REGISTER_FLOW)
    }

    fun saveFlowState(state: String) {
        val context = ContextManager.getInstance().getApplicationContext()

        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            .edit()
            .putString(FLOW_STATE, state)
            .commit()
    }

    fun getPaymentFlowState(): String {
        val context = ContextManager.getInstance().getApplicationContext()

        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            .getString(PAYMENT_FLOW_STATE, FlowManager.INSURANCE_PAYMENT_FLOW)
    }

    fun savePaymentFlowState(state: String) {
        val context = ContextManager.getInstance().getApplicationContext()

        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            .edit()
            .putString(PAYMENT_FLOW_STATE, state)
            .commit()
    }

    fun saveMasterData(masterData: MasterData) {
        val realm = Realm.getInstance(realmConfiguration)
        realm.beginTransaction()
        realm.where(Company::class.java).findAll().deleteAllFromRealm()
        realm.where(SubmitJob::class.java).findAll().deleteAllFromRealm()
        realm.where(RejectJob::class.java).findAll().deleteAllFromRealm()
        realm.where(Termscon::class.java).findAll().deleteAllFromRealm()
        realm.where(Disclosure::class.java).findAll().deleteAllFromRealm()
        realm.where(PromiseLimit::class.java).findAll().deleteAllFromRealm()
        realm.where(HoldLimit::class.java).findAll().deleteAllFromRealm()
        realm.copyToRealm(masterData)
        realm.commitTransaction()
        realm.close()
    }

    fun clear() {
        val context = ContextManager.getInstance().getApplicationContext()
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()

        val realmConfig = RealmConfiguration.Builder()
            .name(DB_NAME)
            .build()

        Realm.deleteRealm(realmConfig)
    }



    fun saveNotify(item: NotifyEntity) {
        val realm = Realm.getInstance(realmConfiguration)
        realm.beginTransaction()
        realm.copyToRealm(item)
        realm.commitTransaction()
        realm.close()
    }

    fun setOverTime(overtime : Boolean) {
        val realm = Realm.getInstance(realmConfiguration)
        realm.beginTransaction()
        realm.delete(UserConfig::class.java)
        val item = UserConfig().apply {
            id = "1"
            isInitial = false
            isLoggedIn = false
            isOverTime = overtime

        }
        realm.copyToRealm(item)
        realm.commitTransaction()
        realm.close()
    }

    fun getIsLoggedin(): UserConfig {
        val realm = Realm.getInstance(realmConfiguration)
        try {
            var entity = realm.where(UserConfig::class.java).findFirst()
            if (entity == null) {
                realm.beginTransaction()
                entity = realm.createObject(UserConfig::class.java)
                realm.commitTransaction()
            }
            val result = entity!!.transform()
            realm.close()
            return result
        } catch (e: Exception) {
            realm.close()
            return UserConfig()
        }

    }

    fun setCurrent(item: CurrentLocationNonCust) {
        val realm = Realm.getInstance(realmConfiguration)
        realm.beginTransaction()
        realm.delete(CurrentLocationNonCust::class.java)
        realm.copyToRealm(item)
        realm.commitTransaction()
        realm.close()
    }


    fun deletePushPopupNotify() {
        val realm = Realm.getInstance(realmConfiguration)

        realm.beginTransaction()
        realm.where(NotifyEntity::class.java)
            .notEqualTo("sequenceId", "")
            .findAll()
            .deleteAllFromRealm()
        realm.commitTransaction()

        realm.close()
    }


    fun getNotifyList(): List<NotifyEntity> {
        val realm = Realm.getInstance(realmConfiguration)
        val result = realm.where(NotifyEntity::class.java)
            .sort("datetime", Sort.DESCENDING)
            .findAll()
            .map { it.transform() }

        realm.close()

        return result
    }




    companion object {
        private val realmManager = DatabaseManager()

        fun init(context: Context) {
            Realm.init(context)
            SecureManager.init(context)
            realmManager.initDatabase()

        }

        fun getInstance() = realmManager
    }

}