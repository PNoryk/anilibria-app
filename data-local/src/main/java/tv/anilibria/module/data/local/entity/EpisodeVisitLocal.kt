package tv.anilibria.module.data.local.entity

data class EpisodeVisitLocal(
    val id: Long,
    val releaseId: Long,
    val playerSeek: Long?,
    val lastOpenAt: Long?
)