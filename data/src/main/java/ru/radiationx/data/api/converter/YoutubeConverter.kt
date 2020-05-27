package ru.radiationx.data.api.converter

import ru.radiationx.data.adomain.entity.youtube.Youtube
import ru.radiationx.data.api.entity.youtube.YouTubeResponse
import ru.radiationx.data.datasource.remote.IApiUtils
import ru.radiationx.shared.ktx.dateFromSec
import toothpick.InjectConstructor

@InjectConstructor
class YoutubeConverter(
    private val apiUtils: IApiUtils
) {

    fun toDomain(response: YouTubeResponse) = Youtube(
        id = response.id,
        title = response.title?.let { apiUtils.escapeHtml(it).toString() },
        image = response.image,
        vid = response.vid,
        views = response.views,
        comments = response.comments,
        timestamp = response.timestamp.dateFromSec()
    )
}