package tv.anilibria.module.data.restapi.entity.mapper

import kotlinx.datetime.Instant
import tv.anilibria.module.data.restapi.entity.app.auth.OtpInfoResponse
import tv.anilibria.module.data.restapi.entity.app.auth.SocialAuthServiceResponse
import tv.anilibria.module.domain.entity.auth.OtpInfo
import tv.anilibria.module.domain.entity.auth.SocialAuthService
import tv.anilibria.core.types.asAbsoluteUrl
import kotlin.time.Duration.Companion.seconds

fun OtpInfoResponse.toDomain() = OtpInfo(
    code = code,
    description = description,
    expiresAt = Instant.fromEpochSeconds(expiresAt),
    remainingTime = remainingTime.seconds
)

fun SocialAuthServiceResponse.toDomain() = SocialAuthService(
    key = key,
    title = title,
    socialUrl = socialUrl.asAbsoluteUrl(),
    resultPattern = Regex(resultPattern),
    errorUrlPattern = Regex(errorUrlPattern),
    color = key.toDataColor(),
    icon = key.toDataIcon()
)