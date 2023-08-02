package com.example.gezdir.data.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Users(
    var id: String? = "",
    var name: String? = "",
    var surname: String? = "",
    var username: String? = "",
    var email: String? = "",
    var password: String? = "",
    var gezdiren: Boolean? = false,
    var profileImage: String? = "images/default.png"
)

