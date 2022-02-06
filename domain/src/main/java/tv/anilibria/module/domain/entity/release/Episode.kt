package tv.anilibria.module.domain.entity.release

data class Episode(
    val id: Int,
    val title: String?,
    val urlSd: String?,
    val urlHd: String?,
    val urlFullHd: String?,
    val srcUrlSd: String?,
    val srcUrlHd: String?,
    val srcUrlFullHd: String?
)