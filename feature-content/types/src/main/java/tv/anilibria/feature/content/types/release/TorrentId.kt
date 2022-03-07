package tv.anilibria.feature.content.types.release

data class TorrentId(
    val id: Long,
    val releaseId: ReleaseId
)