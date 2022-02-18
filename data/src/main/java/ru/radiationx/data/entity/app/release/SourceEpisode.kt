package ru.radiationx.data.entity.app.release

import java.io.Serializable

@Deprecated("old data")
data class SourceEpisode(
    val id: Int,
    val releaseId: Int,
    val title: String?,
    val urlSd: String?,
    val urlHd: String?,
    val urlFullHd: String?
) : Serializable