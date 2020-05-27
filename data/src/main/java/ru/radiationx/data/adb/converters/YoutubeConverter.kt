package ru.radiationx.data.adb.converters

import ru.radiationx.data.adb.entity.youtube.YoutubeDb
import ru.radiationx.data.adomain.entity.youtube.Youtube
import toothpick.InjectConstructor

@InjectConstructor
class YoutubeConverter {

    fun toDomain(source: YoutubeDb) = Youtube(
        id = source.id,
        title = source.title,
        image = source.image,
        vid = source.vid,
        views = source.views,
        comments = source.comments,
        timestamp = source.timestamp
    )

    fun toDb(source: Youtube) = YoutubeDb(
        id = source.id,
        title = source.title,
        image = source.image,
        vid = source.vid,
        views = source.views,
        comments = source.comments,
        timestamp = source.timestamp
    )

    fun toDomain(source: List<YoutubeDb>) = source.map { toDomain(it) }

    fun toDb(source: List<Youtube>) = source.map { toDb(it) }
}