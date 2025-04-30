package com.tlt.georepo.manager

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.*

object LocalizeManager {

    const val ENGLISH = "EN"
    const val THAI = "TH"
    private val PREF_KEY_LANGUAGE = "tlt_language"
    private val PREF_KEY_FIRSTTIME = "tlt_set_language_first_time"
    private lateinit var sharedPreferences: SharedPreferences

    fun init() {
        val context = ContextManager.getInstance().getApplicationContext()
        sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    fun changeToTH(context: Context) {
        setNewLocale(context, THAI)
        ContextManager.getInstance().setApplicationContext(setNewLocale(context, THAI))
    }

    fun changeToEN(context: Context) {
        setNewLocale(context, ENGLISH)
        ContextManager.getInstance().setApplicationContext(setNewLocale(context, ENGLISH))
    }

    private fun setNewLocale(context: Context, language: String): Context {
        sharedPreferences.edit()
                .putString(PREF_KEY_LANGUAGE, language)
                .apply()

        return updateResources(context, language)
    }

    fun initDefaultLocalize(context: Context): Context {
        if (!this::sharedPreferences.isInitialized) {
            init()
        }

        val lang = getLanguage()
        return setNewLocale(context, lang)
    }

    fun getLanguage(): String {
        val lang = sharedPreferences.getString(PREF_KEY_LANGUAGE, "")

        val defaultSystemLang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales.get(0).language
        } else {
            Resources.getSystem().configuration.locale.language
        }

        return when {
            lang.isEmpty() && isSupportLanguage(defaultSystemLang) -> defaultSystemLang.toUpperCase()
            lang.isEmpty() -> ENGLISH
            else -> lang
        }
    }

    fun setIsFirstTime(isFirstTime: Boolean) {
        sharedPreferences.edit()
                .putBoolean(PREF_KEY_FIRSTTIME, isFirstTime)
                .apply()
    }

    fun isFirstTime() = sharedPreferences.getBoolean(PREF_KEY_FIRSTTIME, true)

    fun isThai() = getLanguage() == THAI

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)

        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }

    private fun isSupportLanguage(language: String): Boolean {
        return language.equals(THAI, true) || language.equals(ENGLISH, true)
    }
}