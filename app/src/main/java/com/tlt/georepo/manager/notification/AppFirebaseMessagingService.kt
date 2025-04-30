package com.tlt.georepo.manager.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.threeten.bp.Instant
import com.tlt.georepo.R
import com.tlt.georepo.common.eventbus.UpdateBadgeNotificationEvent
import com.tlt.georepo.manager.BusManager
import com.tlt.georepo.manager.ContextManager
import com.tlt.georepo.manager.JsonMapperManager
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.manager.db.NotifyManager
import com.tlt.georepo.manager.db.UserManager
import com.tlt.georepo.model.response.ItemNotifyPushResponse
import com.tlt.georepo.modules.main.InfoActivity
import com.tlt.georepo.modules.menu.notify.NotifyMainActivity
import java.net.HttpURLConnection
import java.net.URL


class AppFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.e("TEST", remoteMessage!!.data.toString())
        if ((remoteMessage == null || remoteMessage.data == null)
                || remoteMessage.data.isEmpty()) {
            return
        }
        Log.e("TEST", remoteMessage.data.toString())

        try {
            val notify = NotifyManager.saveNotify(remoteMessage.data, remoteMessage.messageId!!)
                BusManager.observe(UpdateBadgeNotificationEvent(UserManager.getInstance().getUnreadNotification()))
            handlePushPopupNotify()
            handleOtherNotify()
            sendNotification(
                notify.title ?: "",
                notify.msg ?: "",
                notify.image ?: "",
                notify.navigation ?: "")
        }catch (e : Exception){
            DatabaseManager.init(this)
            ContextManager.getInstance().setApplicationContext(this)
            val notify = NotifyManager.saveNotify(remoteMessage.data, remoteMessage.messageId!!)
            BusManager.observe(UpdateBadgeNotificationEvent(UserManager.getInstance().getUnreadNotification()))
            handlePushPopupNotify()
            handleOtherNotify()
            showNotification(
                notify.title ?: "",
                notify.msg ?: "",
                notify.image ?: "",
                notify.navigation ?: "")
        }

    }

    private fun handlePushPopupNotify() {
        if (!NotifyManager.isShowPushPopup()) {
            return
        }

        NotifyManager.showPushPopupImmediately()
    }

    private fun handleOtherNotify() {
        if (NotifyManager.isShowPushPopup()) {
            return
        }

        NotifyManager.triggerNotifyImmediately()
    }

    private fun sendNotification(title: String = "",
                                 messageBody: String = "",
                                 imagePath: String = "",
                                 navigation: String = "") {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(navigation))
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.geo_mini)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        if (imagePath.isNotEmpty()) {
            val bitmap = getBitmapFromUrl(imagePath)
            notificationBuilder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        with(NotificationManagerCompat.from(this)) {
            notify(Instant.now().nano, notificationBuilder.build())
        }
    }


    private fun getBitmapFromUrl(imageUrl: String): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showNotification(title: String = "",
                                 messageBody: String = "",
                                 imagePath: String = "",
                                 navigation: String = "") {
        val intent = Intent(this, InfoActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        if (imagePath.isNotEmpty()) {
            val bitmap = getBitmapFromUrl(imagePath)
            notificationBuilder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }


    companion object {
        private val TAG = "AppFirebaseMessagingService"
    }
}