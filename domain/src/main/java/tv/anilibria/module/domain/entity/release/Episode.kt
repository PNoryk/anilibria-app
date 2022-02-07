package tv.anilibria.module.domain.entity.release

import tv.anilibria.module.domain.entity.common.AbsoluteUrl

data class Episode(
    val id: Int,
    val title: AbsoluteUrl?,
    val urlSd: AbsoluteUrl?,
    val urlHd: AbsoluteUrl?,
    val urlFullHd: AbsoluteUrl?,
    val srcUrlSd: AbsoluteUrl?,
    val srcUrlHd: AbsoluteUrl?,
    val srcUrlFullHd: AbsoluteUrl?
)