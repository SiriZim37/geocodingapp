package com.tlt.georepo.manager.notification

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.messaging.FirebaseMessaging
import com.tlt.georepo.common.eventbus.FCMTokenRefreshedEvent
import com.tlt.georepo.manager.db.UserManager
import com.tlt.georepo.modules.main.InfoActivity
import com.tlt.georepo.modules.main.MainMenuActivity
import org.greenrobot.eventbus.EventBus

class AppFirebaseInstanceIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        val token = FirebaseInstanceId.getInstance().token
        Log.e("FirebaseInstanceId" , token)
        EventBus.getDefault().post(FCMTokenRefreshedEvent(token!!))
        FirebaseMessaging.getInstance().subscribeToTopic("GEO_COLLECTION");


    }

    companion object {
        private val TAG = "FCM"
    }
}