//package com.tlt.georepo.manager.Location
//
//import android.content.Intent
//import android.app.IntentService
//import android.support.annotation.Nullable
//import android.util.Log
//import android.app.AlarmManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Context.ALARM_SERVICE
//
//
//
//class AppLocationInstanceService : IntentService("backgroundService") {
//
//    override fun onHandleIntent(@Nullable intent: Intent?) {
//        Log.i("backgroundService", "Service running")
//
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val alarmIntent = Intent(this, AppLocationInstanceService::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0, 1000, pendingIntent)
//
//    }
//}// Used to name the worker thread, important only for debugging.
