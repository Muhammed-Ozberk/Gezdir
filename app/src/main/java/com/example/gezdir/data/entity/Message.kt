package com.example.gezdir.data.entity

import com.google.firebase.Timestamp
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.firestore.FieldValue

@IgnoreExtraProperties
data class Message(
    var messageID: String? = null,
    var senderID: String? = null,
    var receiverID: String? = null,
    var conversationID: String? = null,
    var message: String? = null,
    var date: String? = null,
    var time: String? = null,
    var isSeen: Boolean? = null,
    val timestamp: Any? = null // Server zaman damgası için Any? kullanılır
)
