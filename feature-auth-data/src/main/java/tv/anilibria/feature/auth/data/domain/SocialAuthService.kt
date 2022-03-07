package tv.anilibria.feature.auth.data.domain

import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.feature.content.types.other.DataColor
import tv.anilibria.feature.content.types.other.DataIcon

data class SocialAuthService(
    val key: String,
    val title: String,
    val socialUrl: AbsoluteUrl,
    val resultPattern: Regex,
    val errorUrlPattern: Regex,
    val color: DataColor,
    val icon: DataIcon
)