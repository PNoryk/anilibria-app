package tv.anilibria.module.domain.entity.auth

import tv.anilibria.module.domain.entity.common.AbsoluteUrl

data class SocialAuthService(
    val key: String,
    val title: String,
    val socialUrl: AbsoluteUrl,
    val resultPattern: Regex,
    val errorUrlPattern: Regex
)