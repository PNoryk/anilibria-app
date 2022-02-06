package tv.anilibria.module.data.network.entity.app.release

data class EpisodeResponse(
    val id: Int,
    val title: String?,
    val urlSd: String?,
    val urlHd: String?,
    val urlFullHd: String?,
    val srcUrlSd: String?,
    val srcUrlHd: String?,
    val srcUrlFullHd: String?
)