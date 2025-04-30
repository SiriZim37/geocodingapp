package com.tlt.georepo.manager

import android.app.Application


import android.content.Context
import android.content.Intent
import android.util.Log
import com.tlt.georepo.util.LocaleManager

class LanguageMananger : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleManager.setLocale(this)
    }

}