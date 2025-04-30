//package com.tlt.georepo.manager.Location
//
//import android.widget.Toast
//import android.app.Service
//import android.content.Intent
//import android.os.Handler
//import android.os.IBinder
//import java.util.*
//
//
//class AppLocationService : Service() {
//    private val mHandler = Handler()   //run on another Thread to avoid crash
//    private var mTimer: Timer? = null    //timer handling
//
//    override fun onBind(intent: Intent): IBinder {
//        throw UnsupportedOperationException("Not yet implemented")
//    }
//
//    override fun onCreate() {
//
//        if (mTimer != null)
//        // Cancel if already existed
//            mTimer!!.cancel()
//        else
//            mTimer = Timer()   //recreate new
//
//        val timer = Timer(true)
//        mTimer!!.scheduleAtFixedRate(TimeDisplay(), 1000, 30000)
//        //Schedule
//    }
//
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mTimer!!.cancel()    //For Cancel Timer
//        Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show()
//    }
//
//    //class TimeDisplay for handling task
//    internal inner class TimeDisplay : TimerTask() {
//        override fun run() {
//            // run on another thread
//            mHandler.post(Runnable {
//                // display toast
//                Toast.makeText(this@AppLocationService, "Service is running", Toast.LENGTH_SHORT).show()
//            })
//        }
//    }
//
//    companion object {
//
//        val notify = 30000  //interval between two services(Here Service run every 30 second)
//    }
//}