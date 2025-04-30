package com.tlt.georepo.common.lifecycleobserver

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.app.AppCompatActivity
import com.tlt.georepo.common.eventbus.SendLocationEvent
import com.tlt.georepo.manager.BusManager
import org.greenrobot.eventbus.Subscribe

class LocationLifeCycleObserver(private val activity: AppCompatActivity) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        BusManager.subscribe(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        BusManager.subscribe(this)
    }

    @Subscribe
    fun SendLocationToServer(event: SendLocationEvent) {
    }
}