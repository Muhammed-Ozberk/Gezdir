package com.example.gezdir.util

import java.util.Properties

object ApiKeyHelper {
    fun getFcmApiKey(): String {
        val properties = Properties()
        val inputStream =
            ApiKeyHelper::class.java.classLoader?.getResourceAsStream("apikeys/secrets.properties")
        properties.load(inputStream)
        return properties.getProperty("FCM_API_KEY")
    }
}
