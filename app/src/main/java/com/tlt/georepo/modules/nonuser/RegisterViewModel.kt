package com.tlt.georepo.modules.nonuser

import android.annotation.TargetApi
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.support.annotation.NonNull
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.model2.entity.UserRegisterEntity
import com.tlt.georepo.common.livedata.SingleLiveData
import com.tlt.georepo.manager.ContextManager
import com.tlt.georepo.manager.LocalizeManager
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.manager.db.UserManager
import com.tlt.georepo.model.request.RegistPost
import com.tlt.georepo.util.LocaleManager
import java.util.*

class RegisterViewModel : ViewModel() {
    private val apiManager by lazy { GeoApiManager.getInstance() }
    val whenRegisuccess = SingleLiveData<Boolean>()
    val whenRegisFail = SingleLiveData<Boolean>()

    var errorMessage = ""
    fun getDataLogin(user: String, password: String) {
        if (user != "" && password != "") {
            val entity = UserRegisterEntity().apply {
                id = "1"
                userID = user
                userPass = password
                flagLogin = true
                language = LocalizeManager.getLanguage().toString()
                fcmToken = FirebaseInstanceId.getInstance().token!!
            }
            DatabaseManager.getInstance().saveNewUser(entity)
            whenRegisuccess.postValue(true)
        } else {
            whenRegisFail.postValue(true)
        }
    }

    fun Register(request: RegistPost) {

        apiManager.getRegister(request) { isError: Boolean, result: String ->
            try {
                if (isError) {
                    whenRegisFail.postValue(true)
                    return@getRegister
                }
                try {
                    if (result.equals("success")) {
                        val user = UserManager.getInstance().getProfile().apply {
                            this.userID = "1"
                            this.token = DatabaseManager.getInstance().getUserInfo().token
                            this.userName = request.pkeY1!! + " " + request.pkeY2!!
                            this.userEmail = request.pkeY3!!
                            this.userPhone = request.pkeY5!!
                            this.userIDCard = request.pkeY4!!
                            this.userStartDate = request.pkeY8!!
                            this.userCompany = request.pkeY7!!
                            this.userBirthDate = request.pkeY6!!
                            this.userCompletionDate = request.pkeY9!!
                            this.salt = ""
                        }
                        UserManager.getInstance().saveProfile(user)
                        whenRegisuccess.postValue(true)
                    }else{
                        whenRegisFail.postValue(true)
                    }
                } catch (e: Exception) {
                    Log.d("Error Connect API : ", e.stackTrace.toString())
                    Log.d("Error Connect API 2 : ", e.toString() + " and " + e.printStackTrace())
                }
                return@getRegister
            } catch (e: Exception) {
                Log.e("getPullJobCollection : ", e.stackTrace.toString())
            }
        }
    }

    fun isValidPhone(phone: CharSequence): Boolean {
        return (phone.isNotEmpty() && phone.length == 10)
    }


    fun isValidEmail(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

     fun setLanguage(id: Int): String {
        Log.e("sharedPref_lang", LocaleManager.getLanguagePref(ContextManager.getInstance().getApplicationContext()))
        var sharedPref_lang = LocaleManager.getLanguagePref(ContextManager.getInstance().getApplicationContext())
        errorMessage = getStringByLocal(ContextManager.getInstance().getApplicationContext(),id,sharedPref_lang)
         return errorMessage
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun getStringByLocal(context: Context, id: Int, locale: String): String {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(Locale(locale))
        return context.createConfigurationContext(configuration).resources.getString(id)
    }

}