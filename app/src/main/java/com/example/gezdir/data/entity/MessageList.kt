package com.example.gezdir.data.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class MessageList(
    var username: String? = "",
    var lastMessage: String? = "",
    var conversationID: String? = "",
    var receiverID: String? = "",
    var image: String? = ""
)
