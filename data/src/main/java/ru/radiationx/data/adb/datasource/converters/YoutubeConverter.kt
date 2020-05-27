package ru.radiationx.data.adb.datasource.converters

import ru.radiationx.data.adb.youtube.YouTubeDb
import ru.radiationx.data.adomain.youtube.YouTube
import toothpick.InjectConstructor

@InjectConstructor
class YoutubeConverter {

    fun toDomain(source: YouTubeDb) = YouTube(
        id = source.id,
        title = source.title,
        image = source.image,
        vid = source.vid,
        views = source.views,
        comments = source.comments,
        timestamp = source.timestamp
    )

    fun toDb(source: YouTube) = YouTubeDb(
        id = source.id,
        title = source.title,
        image = source.image,
        vid = source.vid,
        views = source.views,
        comments = source.comments,
        timestamp = source.timestamp
    )

    fun toDomain(source: List<YouTubeDb>) = source.map { toDomain(it) }

    fun toDb(source: List<YouTube>) = source.map { toDb(it) }
}