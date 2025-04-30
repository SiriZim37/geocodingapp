package com.tlt.georepo.manager

import org.greenrobot.eventbus.EventBus

object BusManager {
    fun subscribe(any : Any) {
        if (EventBus.getDefault().isRegistered(any)) {
            return
        }

        EventBus.getDefault().register(any)
    }

    fun unsubscribe(any : Any) {
        if (!EventBus.getDefault().isRegistered(any)) {
            return
        }

        EventBus.getDefault().unregister(any)
    }

    fun observe(any : Any) {
        EventBus.getDefault().post(any)
    }
}