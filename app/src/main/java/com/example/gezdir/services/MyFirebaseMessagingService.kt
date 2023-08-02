package com.example.gezdir.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.gezdir.R
import com.example.gezdir.ui.component.openingScreen.OpeningScreenActivity
import com.example.gezdir.util.UserManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let { notification ->
            val title = notification.title ?: "Varsayılan Başlık"
            val body = notification.body ?: "Varsayılan İçerik"

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = "kanal_id"
            val contentIntent = PendingIntent.getActivity(
                applicationContext,
                1,
                Intent(applicationContext, OpeningScreenActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(applicationContext, channelId)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.baseline_ac_unit_24)
                .setContentText(body)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val channelName = "Kanal Adı"
                        val channelDescription = "Kanal Açıklaması"
                        val importance = NotificationManager.IMPORTANCE_HIGH
                        val channel = NotificationChannel(channelId, channelName, importance)
                        channel.description = channelDescription
                        notificationManager.createNotificationChannel(channel)
                    } else {
                        priority = Notification.PRIORITY_HIGH
                    }
                }

            notificationManager.notify(1, builder.build())
        }
    }

    override fun onNewToken(token: String) {
        if (UserManager.currentUserId.isNotEmpty()) {
            val collectionRef = FirebaseFirestore.getInstance().collection("fcmTokens")
            val tokenObj = Token(UserManager.currentUserId, token)
            collectionRef.document(UserManager.currentUserId).set(tokenObj)
                .addOnSuccessListener {
                    Log.e("FCM", "Token kaydedildi")
                }
                .addOnFailureListener { exception ->
                    Log.e("FCM", "Token kaydedilemedi: ${exception.message}")
                }
        }
    }

}

data class Token(val userID: String, val token: String)
