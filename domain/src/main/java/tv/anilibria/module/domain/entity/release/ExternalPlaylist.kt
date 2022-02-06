package tv.anilibria.module.domain.entity.release

data class ExternalPlaylist(
    val tag: String,
    val title: String,
    val actionText: String,
    val episodes: List<ExternalEpisode>
) 