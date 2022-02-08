package tv.anilibria.module.domain.entity.release

import tv.anilibria.core.types.AbsoluteUrl

data class EpisodeId(val id: Long)

data class Episode(
    val id: EpisodeId,
    val releaseId: ReleaseId,
    val title: String?,
    val urlSd: AbsoluteUrl?,
    val urlHd: AbsoluteUrl?,
    val urlFullHd: AbsoluteUrl?,
    val srcUrlSd: AbsoluteUrl?,
    val srcUrlHd: AbsoluteUrl?,
    val srcUrlFullHd: AbsoluteUrl?
)