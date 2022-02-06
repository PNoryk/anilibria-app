package tv.anilibria.module.domain.entity.release

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
    val size: Long,
    val url: String?
)