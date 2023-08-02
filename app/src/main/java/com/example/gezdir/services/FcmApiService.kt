package com.example.gezdir.services

import com.example.gezdir.data.entity.FcmNotification
import com.example.gezdir.data.entity.FcmResponse
import com.example.gezdir.util.ApiKeyHelper
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FcmApiService {
    @POST("fcm/send")
    fun sendNotification(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") authorization: String = "key=" + ApiKeyHelper.getFcmApiKey(),
        @Body notification: FcmNotification
    ): Call<FcmResponse>
}
