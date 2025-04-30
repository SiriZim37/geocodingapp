//package com.tlt.georepo.manager.Location
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.widget.Toast
//import android.content.Intent
//
//
//class AppLocationBroadcastReceiver : BroadcastReceiver() {
//
//    override fun onReceive(context: Context, intent: Intent) {
//        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
//
//            val serviceIntent = Intent(context, AppLocationInstanceService::class.java)
//            context.startService(serviceIntent)
//        } else {
//            Toast.makeText(context.getApplicationContext(), "Alarm Manager just ran", Toast.LENGTH_LONG).show()
//        }
//
//    }
//}