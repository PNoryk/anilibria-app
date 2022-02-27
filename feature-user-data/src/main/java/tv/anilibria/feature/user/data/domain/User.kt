package tv.anilibria.feature.user.data.domain

import tv.anilibria.core.types.RelativeUrl

data class User(
    val id: UserId,
    val avatar: RelativeUrl?,
    val login: String
)
