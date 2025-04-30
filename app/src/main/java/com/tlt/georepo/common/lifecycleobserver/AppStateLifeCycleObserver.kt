package com.tlt.georepo.common.lifecycleobserver

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.tlt.georepo.common.eventbus.SendLocationEvent
import com.tlt.georepo.manager.BusManager
import com.tlt.georepo.manager.db.DatabaseManager

class AppStateLifeCycleObserver : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onForeground() {
        BusManager.observe(SendLocationEvent())
        DatabaseManager.getInstance().updateAppState(true)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackground() {
        BusManager.observe(SendLocationEvent())
        DatabaseManager.getInstance().updateAppState(false)
    }
}