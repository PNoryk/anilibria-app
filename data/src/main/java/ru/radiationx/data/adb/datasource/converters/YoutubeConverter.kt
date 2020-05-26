package ru.radiationx.data.adb.datasource.converters

import ru.radiationx.data.adb.youtube.YouTubeDb
import ru.radiationx.data.adomain.youtube.YouTube
import toothpick.InjectConstructor

@InjectConstructor
class YoutubeConverter {

    fun toDomain(youTubeDb: YouTubeDb) = YouTube(
        id = youTubeDb.id,
        title = youTubeDb.title,
        image = youTubeDb.image,
        vid = youTubeDb.vid,
        views = youTubeDb.views,
        comments = youTubeDb.comments,
        timestamp = youTubeDb.timestamp
    )

    fun toDb(youTube: YouTube) = YouTubeDb(
        id = youTube.id,
        title = youTube.title,
        image = youTube.image,
        vid = youTube.vid,
        views = youTube.views,
        comments = youTube.comments,
        timestamp = youTube.timestamp
    )


    fun toDomain(items: List<YouTubeDb>) = items.map { toDomain(it) }

    fun toDb(items: List<YouTube>) = items.map { toDb(it) }
}