package com.tlt.georepo.common.lifecycleobserver

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import org.greenrobot.eventbus.Subscribe
import com.tlt.georepo.R
import com.tlt.georepo.common.eventbus.NoInternetEvent
import com.tlt.georepo.manager.BusManager
import com.tlt.georepo.manager.ContextManager

class NoInternetLifeCycleobserver(private val activity: AppCompatActivity) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        BusManager.subscribe(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        BusManager.unsubscribe(this)
    }

    @Subscribe
    fun onNoInternetReceived(event: NoInternetEvent) {
        Toast.makeText(activity.applicationContext, ContextManager.getInstance().getApplicationContext().resources.getString(R.string.alert_no_internet_title), Toast.LENGTH_SHORT).show()
    }

}