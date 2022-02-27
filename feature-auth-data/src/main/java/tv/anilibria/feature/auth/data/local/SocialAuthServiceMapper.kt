package tv.anilibria.feature.auth.data.local

import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.module.data.local.mappers.toDataColor
import tv.anilibria.module.data.local.mappers.toDataIcon
import tv.anilibria.module.data.local.mappers.toLocal
import tv.anilibria.feature.auth.data.domain.SocialAuthService

fun SocialAuthService.toLocal() = SocialAuthServiceLocal(
    key = key,
    title = title,
    socialUrl = socialUrl.value,
    resultPattern = resultPattern.pattern,
    errorUrlPattern = errorUrlPattern.pattern,
    color = color.toLocal(),
    icon = icon.toLocal()
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