package tv.anilibria.module.domain.entity.other

import tv.anilibria.module.domain.entity.common.RelativeUrl

data class UserId(val id: Long)

data class User(
    val id: UserId,
    val avatar: RelativeUrl?,
    val login: String?
)
