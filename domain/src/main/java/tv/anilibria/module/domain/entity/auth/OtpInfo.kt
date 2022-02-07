package tv.anilibria.module.domain.entity.auth

import kotlinx.datetime.Instant
import kotlin.time.Duration

data class OtpInfo(
    val code: String,
    val description: String,
    val expiresAt: Instant,
    val remainingTime: Duration
)