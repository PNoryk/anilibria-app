package tv.anilibria.module.data.network.entity.app.release

import java.io.Serializable

data class ExternalPlaylistResponse(
    val tag: String,
    val title: String,
    val actionText: String,
    val episodes: List<ExternalEpisodeResponse>
) : Serializable