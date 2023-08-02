package com.example.gezdir.data.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Advert(
    var id: String? = "",
    var username: String? = "",
    var title: String? = "",
    var content: String? = "",
    var time:String? = "",
    var price: String? = "",
    var image: String? = "",
)