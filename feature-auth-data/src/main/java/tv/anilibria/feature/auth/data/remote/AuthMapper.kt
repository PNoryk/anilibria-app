package tv.anilibria.feature.auth.data.remote

import kotlinx.datetime.Instant
import tv.anilibria.core.types.asAbsoluteUrl
import tv.anilibria.feature.auth.data.domain.OtpInfo
import tv.anilibria.feature.auth.data.domain.SocialAuthService
import tv.anilibria.feature.content.data.remote.entity.mapper.toDataColor
import tv.anilibria.feature.content.data.remote.entity.mapper.toDataIcon
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