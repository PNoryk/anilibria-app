package tv.anilibria.module.domain.entity.other

import tv.anilibria.module.domain.entity.common.RelativeUrl

data class User(
    val id: Int,
    val avatar: RelativeUrl?,
    val login: String?
)
