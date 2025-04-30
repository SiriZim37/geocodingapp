package com.tlt.georepo.util
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log

import java.util.Locale

object LocaleManager {
    /**
     * For english locale
     */
    val LANGUAGE_KEY_ENGLISH = "en"

    val LANGUAGE_KEY_THAI = "th"
    /**
     * for hindi locale
     */
    val LANGUAGE_KEY_HINDI = "hi"
    /***
     * // for spanish locale
     */

    val LANGUAGE_KEY_SPANISH = "es"
    /**
     * SharedPreferences Key
     */
    private val LANGUAGE_KEY = "language_key"

    /**
     * set current pref locale
     * @param mContext
     * @return
     */
    fun setLocale(mContext: Context): Context {
        return updateResources(mContext, getLanguagePref(mContext))
    }

    /**
     * Set new Locale with context
     * @param mContext
     * @param mLocaleKey
     * @return
     */
    fun setNewLocale(mContext: Context, mLocaleKey: String): Context {
        setLanguagePref(mContext, mLocaleKey)
        return updateResources(mContext, mLocaleKey)
    }

    /**
     * Get saved Locale from SharedPreferences
     * @param mContext current context
     * @return current locale key by default return english locale
     */
    fun getLanguagePref(mContext: Context): String {
        val mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        return mPreferences.getString(LANGUAGE_KEY, LANGUAGE_KEY_ENGLISH)
    }

    /**
     * set pref key
     * @param mContext
     * @param localeKey
     */
    private fun setLanguagePref(mContext: Context, localeKey: String) {
        val mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        mPreferences.edit().putString(LANGUAGE_KEY, localeKey).commit()
    }

    /**
     * update resource
     * @param context
     * @param language
     * @return
     */
    private fun updateResources(context: Context, language: String): Context {
        var context = context
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res = context.resources
        Log.e("language", language)
        val config = Configuration(res.configuration)
//        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale)
            context = context.createConfigurationContext(config)
//        } else {
            config.locale = locale
            res.updateConfiguration(config, res.displayMetrics)
        Log.e("Locale", locale.language)
//        }
        return context
    }

    /**
     * get current locale
     * @param res
     * @return
     */
    fun getLocale(res: Resources): Locale {
        val config = res.configuration
        return if (Build.VERSION.SDK_INT >= 24) config.locales.get(0) else config.locale
    }

}
