package tv.anilibria.module.data.network.entity.app.auth

import java.util.*

data class OtpInfoResponse(
    val code: String,
    val description: String,
    val expiresAt: Int,
    val remainingTime: Int
)