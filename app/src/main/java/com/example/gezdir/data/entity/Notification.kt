package com.example.gezdir.data.entity

import com.example.gezdir.util.UserManager
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Notification(
    var username: String? = "",
    var lastMessage: String? = "",
    var conversationID: String? = "",
    var senderID: String? = "",
    var receiverID: String? = "",
    var senderImage: String? = UserManager.currentUserProfileUrl
)
