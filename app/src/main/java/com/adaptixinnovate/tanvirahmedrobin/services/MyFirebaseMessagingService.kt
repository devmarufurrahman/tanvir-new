package com.adaptixinnovate.tanvirahmedrobin.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.adaptixinnovate.tanvirahmedrobin.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.URL

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let {
            showNotification(it.title, it.body, message.data["image"])
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Handle the new FCM registration token here
        Log.d("FCM Token", "New Token: $token")
    }


    private fun showNotification(title: String?, body: String?, imageUrl: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "fcm_default_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For Android 8.0 and above
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "This is the default notification channel"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Notification builder for all versions
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title ?: "Notification Title")
            .setContentText(body ?: "Notification Body")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        imageUrl?.let { url ->
            try {

                val bitmap = URL(url).openStream().use {
                    BitmapFactory.decodeStream(it)
                }

                val bigPictureStyle = NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .setBigContentTitle(title)
                    .setSummaryText(body)

                notificationBuilder.setStyle(bigPictureStyle)
            } catch (e: Exception) {
                Log.e("FCM", "get image error: ${e.message}")
            }
        }

        // Show notification
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

}