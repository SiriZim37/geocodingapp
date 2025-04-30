package com.tlt.georepo

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.ProcessLifecycleOwner
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import android.support.v7.app.AppCompatDelegate
import com.crashlytics.android.Crashlytics
import com.github.ajalt.reprint.core.Reprint
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tlt.georepo.common.lifecycleobserver.AppStateLifeCycleObserver
import me.yokeyword.fragmentation.Fragmentation
//import com.tlt.georepo.common.lifecycleobserver.AppStateLifeCycleObserver
import com.tlt.georepo.manager.ContextManager
import com.tlt.georepo.manager.LocalizeManager
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.manager.notification.AppNotificationManager
import io.fabric.sdk.android.Fabric

class MainApplication : MultiDexApplication(), Application.ActivityLifecycleCallbacks {

    private var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        initProviders()
    }

    private fun initProviders() {
        AndroidThreeTen.init(this)
        ContextManager.getInstance().setApplicationContext(this)
        Reprint.initialize(this)
        LocalizeManager.initDefaultLocalize(this)
        ContextManager.getInstance().setApplicationContext(LocalizeManager.initDefaultLocalize(this))
        DatabaseManager.init(this)
        Fragmentation.builder().install()
        AppNotificationManager.subscribe(
            "all",
            BuildConfig.FCM_GEO_TOPIC_ANDROID
        )
        AppNotificationManager.subscribe(
            "all",
            BuildConfig.FCM_GEO_TOPIC
        )
        ProcessLifecycleOwner.get()
            .lifecycle
            .addObserver(AppStateLifeCycleObserver())

        setupCrashlytic()
    }

    private fun setupCrashlytic() {
        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(false)  // Enables Crashlytics debugger
                .build()
        Fabric.with(fabric)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {
        currentActivity = activity
    }

    override fun onActivityStarted(activity: Activity?) {
        currentActivity = activity
    }

    override fun onActivityDestroyed(activity: Activity?) {

    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStopped(activity: Activity?) {

    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        currentActivity = activity
    }

    fun getCurrentActivity(): Activity? {
        return currentActivity
    }

    companion object {
        fun restart(baseContext: Context) {
            val intent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            baseContext.startActivity(intent)
        }
    }
}