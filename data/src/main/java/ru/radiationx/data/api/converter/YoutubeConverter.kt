package ru.radiationx.data.api.converter

import ru.radiationx.data.adomain.YouTube
import ru.radiationx.data.api.remote.YouTubeResponse
import ru.radiationx.shared.ktx.dateFromSec

class YoutubeConverter {

    fun toDomain(response: YouTubeResponse) = YouTube(
        id = response.id,
        title = response.title,
        image = response.image,
        vid = response.vid,
        views = response.views,
        comments = response.comments,
        timestamp = response.timestamp.dateFromSec()
    )
}