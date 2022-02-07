package tv.anilibria.module.domain.entity.release

import tv.anilibria.module.domain.entity.common.Bytes
import tv.anilibria.module.domain.entity.common.RelativeUrl

/**
 * Created by radiationx on 30.01.18.
 */
data class Torrent(
    val id: Int,
    val hash: String?,
    val leechers: Int,
    val seeders: Int,
    val completed: Int,
    val quality: String?,
    val series: String?,
    val size: Bytes,
    val url: RelativeUrl?
)