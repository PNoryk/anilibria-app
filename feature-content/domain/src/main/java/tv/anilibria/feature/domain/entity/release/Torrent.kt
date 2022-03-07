package tv.anilibria.feature.domain.entity.release

import tv.anilibria.core.types.Bytes
import tv.anilibria.core.types.Count
import tv.anilibria.core.types.RelativeUrl

data class Torrent(
    val id: TorrentId,
    val hash: String?,
    val leechers: Count,
    val seeders: Count,
    val completed: Count,
    val quality: String?,
    val series: String?,
    val size: Bytes,
    val url: RelativeUrl?
)