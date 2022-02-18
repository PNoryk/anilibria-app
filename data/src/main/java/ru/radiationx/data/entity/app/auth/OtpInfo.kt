package ru.radiationx.data.entity.app.auth

import java.util.*

@Deprecated("old data")
data class OtpInfo(
    val code: String,
    val description: String,
    val expiresAt: Date,
    val remainingTime: Long
)