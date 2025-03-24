package com.example.cinelog.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.cinelog.R
import com.example.cinelog.data.local.sharedPref.SharedPrefHelper
import com.example.cinelog.ui.home.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.HttpURLConnection
import java.net.URL

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var sharedPrefHelper: SharedPrefHelper

    override fun onCreate() {
        super.onCreate()
        // Initialize SharedPrefHelper in onCreate() to ensure context is available
        sharedPrefHelper = SharedPrefHelper(applicationContext)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)


        val title = remoteMessage.notification?.title ?: "New Notification"
        val message = remoteMessage.notification?.body ?: "You have a new update."
        val imageUrl = remoteMessage.data["posterUrl"] // Optional image
        Log.d("testing", "Message received: ${remoteMessage.data}")
        // Increment notification count
        incrementNotificationCount()
        // Send notification
        sendNotification(title, message, imageUrl)

    }

    private fun incrementNotificationCount() {
        val currentCount = sharedPrefHelper.getNotificationCount()
        val newCount = currentCount + 1
        sharedPrefHelper.setNotification(newCount)
        // Send broadcast to update UI
        val intent = Intent("com.example.cinelog.UPDATE_NOTIFICATION_COUNT").apply {
            putExtra("notification_count", newCount)
        }
        sendBroadcast(intent)
    }

    private fun sendNotification(title: String, message: String, imageUrl: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "movie_notifications"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification) // Use your app icon
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        // Load image if available
        if (!imageUrl.isNullOrEmpty()) {
            val bitmap = getBitmapFromUrl(imageUrl)
            if (bitmap != null) {
                notificationBuilder.setStyle(
                    NotificationCompat.BigPictureStyle().bigPicture(bitmap).setSummaryText(message)
                )
            }
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // For Android Oreo and above, create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Movie Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun getBitmapFromUrl(imageUrl: String): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.e("FCM", "Error loading image: $e")
            null
        }
    }
}
