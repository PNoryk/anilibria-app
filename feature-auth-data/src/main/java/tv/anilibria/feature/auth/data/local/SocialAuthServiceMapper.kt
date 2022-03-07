package tv.anilibria.feature.auth.data.local

import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.feature.auth.data.domain.SocialAuthService
import tv.anilibria.feature.content.types.other.toDataColor
import tv.anilibria.feature.content.types.other.toDataIcon
import tv.anilibria.feature.content.types.other.toKey

fun SocialAuthService.toLocal() = SocialAuthServiceLocal(
    key = key,
    title = title,
    socialUrl = socialUrl.value,
    resultPattern = resultPattern.pattern,
    errorUrlPattern = errorUrlPattern.pattern,
    color = color.toKey(),
    icon = icon.toKey()
)

fun SocialAuthServiceLocal.toDomain() = SocialAuthService(
    key = key,
    title = title,
    socialUrl = AbsoluteUrl(value = socialUrl),
    resultPattern = resultPattern.toRegex(),
    errorUrlPattern = errorUrlPattern.toRegex(),
    color = color.toDataColor(),
    icon = icon.toDataIcon()
)