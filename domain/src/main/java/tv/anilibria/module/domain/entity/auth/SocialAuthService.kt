package tv.anilibria.module.domain.entity.auth

import tv.anilibria.module.domain.entity.common.AbsoluteUrl
import tv.anilibria.module.domain.entity.other.DataColor
import tv.anilibria.module.domain.entity.other.DataIcon

data class SocialAuthService(
    val key: String,
    val title: String,
    val socialUrl: AbsoluteUrl,
    val resultPattern: Regex,
    val errorUrlPattern: Regex,
    val color: DataColor,
    val icon: DataIcon
)