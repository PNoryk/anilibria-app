package tv.anilibria.module.data.network.entity.app.release

import java.io.Serializable

data class ExternalEpisode(
    val id: Int,
    val releaseId: Int,
    val title: String?,
    val url: String?
) : Serializable