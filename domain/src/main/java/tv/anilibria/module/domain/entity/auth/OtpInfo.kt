package tv.anilibria.module.domain.entity.auth

data class OtpInfo(
    val code: String,
    val description: String,
    val expiresAt: Int,
    val remainingTime: Int
)