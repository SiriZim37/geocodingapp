package com.tlt.georepo.common.lifecycleobserver

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.app.AppCompatActivity
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.manager.db.UserManager
import com.tlt.georepo.modules.pincode.AuthenPincodeActivity

class ReAuthLifeCycleObserver(private val activity: AppCompatActivity) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        val isAppStateForeground = DatabaseManager.getInstance().isAppStateForeground()
        val userManager = UserManager.getInstance()
        val userlogin = DatabaseManager.getInstance().getUserInfo()

        if (!userlogin.flagLogin
                || isAppStateForeground
                || !userManager.isLastActiveOverLimit5mins()) {
            return
        }

        AuthenPincodeActivity.open(activity)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        UserManager.getInstance().updateLastActive()
    }
}