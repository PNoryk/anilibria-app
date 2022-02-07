package tv.anilibria.module.domain.entity.release

import tv.anilibria.module.domain.entity.common.Bytes
import tv.anilibria.module.domain.entity.common.RelativeUrl

data class TorrentId(val id: Long)

data class Torrent(
    val id: TorrentId,
    val releaseId: ReleaseId,
    val hash: String?,
    val leechers: Int,
    val seeders: Int,
    val completed: Int,
    val quality: String?,
    val series: String?,
    val size: Bytes,
    val url: RelativeUrl?
)