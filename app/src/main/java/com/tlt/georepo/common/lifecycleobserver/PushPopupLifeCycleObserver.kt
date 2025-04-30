package com.tlt.georepo.common.lifecycleobserver

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.app.AppCompatActivity
import com.tlt.georepo.common.eventbus.PushPopupEvent
import com.tlt.georepo.manager.BusManager
import org.greenrobot.eventbus.Subscribe

class PushPopupLifeCycleObserver(private val activity: AppCompatActivity) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        BusManager.subscribe(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        BusManager.unsubscribe(this)
    }

    @Subscribe
    fun onPushPopupReceived(event: PushPopupEvent) {

    }
}