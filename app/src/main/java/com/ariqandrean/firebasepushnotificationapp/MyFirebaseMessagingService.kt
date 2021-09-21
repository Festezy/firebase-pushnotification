package com.ariqandrean.firebasepushnotificationapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId = 101
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        //..

        Log.d(TAG, "From: ${p0.from}")

        // check if messages a data payload
        if (p0.data.size > 0){
            val title = p0.data["title"]
            val body = p0.data["body"]
            showNotification(applicationContext, title, body)
        } else {
            val title = p0.notification!!.title
            val body = p0.notification!!.body
            showNotification(applicationContext, title, body)
        }

        p0.notification?.let {
            Log.d(TAG, "Messages Notification Body: ${it.body}")
        }
        // also
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d(TAG, "Refresh token: $p0")
    }

    fun showNotification(context: Context, title: String?, message: String?){
        val ii: Intent
        ii = Intent(context, MainActivity::class.java)
        ii.data = Uri.parse("custom://" + System.currentTimeMillis())
        ii.action = "actionstring" + System.currentTimeMillis()
        ii.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        var pi = PendingIntent.getActivities(context, 0, arrayOf(ii), PendingIntent.FLAG_UPDATE_CURRENT)
        val notification: Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title).build()
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            val notificationChannel = NotificationChannel(CHANNEL_ID, title, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(notificationId, notification)
        } else {
            notification = NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText(message)
                .setContentIntent(pi)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title).build()

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.notify(notificationId, notification)
        }
    }
}