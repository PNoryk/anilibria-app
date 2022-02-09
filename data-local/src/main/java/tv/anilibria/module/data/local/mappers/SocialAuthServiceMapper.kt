package tv.anilibria.module.data.local.mappers

import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.module.data.local.entity.SocialAuthServiceLocal
import tv.anilibria.module.domain.entity.auth.SocialAuthService

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