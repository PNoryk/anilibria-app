package anilibria.tv.domain.entity.episode


data class Episode(
    val releaseId: Int,
    val id: Int,
    val title: String?,
    val sd: String?,
    val hd: String?,
    val fullhd: String?,
    val srcSd: String?,
    val srcHd: String?
)