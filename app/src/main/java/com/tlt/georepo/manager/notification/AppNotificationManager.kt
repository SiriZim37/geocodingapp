package com.tlt.georepo.manager.notification

import com.google.firebase.messaging.FirebaseMessaging

class AppNotificationManager private constructor() {

    companion object {
        fun subscribe(topic: String = "all") {
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
        }

        fun subscribe(vararg topics: String) {
            topics.forEach {
                FirebaseMessaging.getInstance().subscribeToTopic(it)
            }
        }
    }
}