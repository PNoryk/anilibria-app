package tv.anilibria.module.data.network.entity.mapper

import tv.anilibria.module.data.network.entity.app.auth.OtpInfoResponse
import tv.anilibria.module.data.network.entity.app.auth.SocialAuthServiceResponse
import tv.anilibria.module.domain.entity.auth.OtpInfo
import tv.anilibria.module.domain.entity.auth.SocialAuthService

fun OtpInfoResponse.toDomain() = OtpInfo(
    code = code,
    description = description,
    expiresAt = expiresAt,
    remainingTime = remainingTime
)

fun SocialAuthServiceResponse.toDomain() = SocialAuthService(
    key = key,
    title = title,
    socialUrl = socialUrl,
    resultPattern = resultPattern,
    errorUrlPattern = errorUrlPattern
)