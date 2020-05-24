package ru.radiationx.data.adomain.auth

import java.util.*

data class OtpInfo(
    val code: String,
    val description: String,
    val expiresAt: Date,
    val remainingTime: Long
)