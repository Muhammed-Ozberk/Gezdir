package com.example.gezdir.data.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Conversation(
    var conversationID: String? = "",
    var userOne: String? = "",
    var userTwo: String? = "",
    var seen: String? = "",
    var lastMessage: String? = "",
)
