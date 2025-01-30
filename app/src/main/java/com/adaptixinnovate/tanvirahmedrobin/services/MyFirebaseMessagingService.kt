package com.adaptixinnovate.tanvirahmedrobin.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.adaptixinnovate.tanvirahmedrobin.R
import com.adaptixinnovate.tanvirahmedrobin.constants.AppConfig
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let {
            showNotification(it.title, it.body, "${AppConfig.IMG_URL}${message.data["image"]}")
//            showNotification(it.title, it.body, "${message.data["image"]}")
        }
        Log.d("NotificationMsg", "onMessageReceived: ${AppConfig.IMG_URL}${message.data["image"]}")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM Token", "New Token: $token")
    }

    private fun showNotification(title: String?, body: String?, imageUrl: String?) {

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "fcm_default_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Default Channel"
            val channelDescription = "This is the default notification channel"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Notification Builder
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title ?: "Notification Title")
            .setContentText(body ?: "Notification Body")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // Image Load & Make Rounded
        if (!imageUrl.isNullOrEmpty()) {
            Thread {
                try {
                    val bitmap = URL(imageUrl).openStream().use { BitmapFactory.decodeStream(it) }
                    val roundedBitmap = getRoundedCornerBitmap(bitmap, 50)

                    val bigPictureStyle = NotificationCompat.BigPictureStyle()
                        .bigPicture(roundedBitmap)
                        .setBigContentTitle(title)
                        .setSummaryText(body)

                    notificationBuilder.setStyle(bigPictureStyle)

                    notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
                } catch (e: Exception) {
                    Log.e("FCM", "Error loading image: ${e.message}")
                    notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
                }
            }.start()
            Log.d("TAG", "img1")
        } else {
            Log.d("TAG", "img2")
            notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
        }


    }



    // Function to Make Image Rounded
    private fun getRoundedCornerBitmap(bitmap: Bitmap, radius: Int): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)

        paint.isAntiAlias = true
        canvas.drawRoundRect(rectF, radius.toFloat(), radius.toFloat(), paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }
}
