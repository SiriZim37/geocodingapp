package com.tlt.georepo.modules.nonuser

import android.annotation.TargetApi
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.support.annotation.NonNull
import android.util.Log
import com.tlt.georepo.manager.ContextManager
import com.tlt.georepo.util.LocaleManager
import java.util.*

class TermsAndConsViewModel : ViewModel(){
    var errorMessage = ""
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