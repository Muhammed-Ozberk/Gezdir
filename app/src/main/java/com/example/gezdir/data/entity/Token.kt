package com.example.gezdir.data.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Token(var userID:String? = "", var token:String? = "")
