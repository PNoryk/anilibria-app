package ru.radiationx.data.entity.app.release

import java.io.Serializable

@Deprecated("old data")
data class ExternalEpisode(
    val id: Int,
    val releaseId: Int,
    val title: String?,
    val url: String?
) : Serializable