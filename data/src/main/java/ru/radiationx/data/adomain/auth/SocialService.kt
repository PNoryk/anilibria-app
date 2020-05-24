package ru.radiationx.data.adomain.auth

data class SocialService(
    val key: String,
    val title: String,
    val socialUrl: String,
    val resultPattern: String,
    val errorUrlPattern: String
)