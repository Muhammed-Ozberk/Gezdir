package com.example.gezdir.data.entity

data class FcmNotification(
    val to: String, // Hedef cihazın FCM token'ı
    val notification: FcmNotificationData
)