package ru.radiationx.anilibria.common

import android.content.Context
import android.text.format.DateUtils
import ru.radiationx.data.entity.app.feed.FeedItem
import tv.anilibria.module.domain.entity.feed.Feed
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.module.domain.entity.youtube.Youtube
import java.util.*

class CardsDataConverter(
    private val context: Context
) {

    fun toCard(releaseItem: Release): LibriaCard {
        val seasonText = releaseItem.season?.value
        val genreText = releaseItem.genres?.firstOrNull()?.value?.capitalize()
        val seriesText = releaseItem.series?.trim() ?: "Не доступно"
        val dateText = releaseItem.torrentUpdate
            ?.toEpochMilliseconds()
            ?.let { Date(it).relativeDate(context).decapitalize() }
        return releaseItem.run {
            LibriaCard(
                id.id,
                names?.firstOrNull()?.text.orEmpty(),
                "$seasonText год • $genreText • Серии: $seriesText • Обновлен $dateText",
                poster?.url.orEmpty(),
                LibriaCard.Type.RELEASE
            ).apply {
                rawData = releaseItem
            }
        }
    }

    fun toCard(youtubeItem: Youtube) = youtubeItem.run {
        LibriaCard(
            id.id,
            title?.text.orEmpty(),
            "Вышел ${Date(timestamp.toEpochMilliseconds()).relativeDate(context).decapitalize()}",
            image?.url.orEmpty(),
            LibriaCard.Type.YOUTUBE
        ).apply {
            rawData = youtubeItem
        }
    }

    fun toCard(feedItem: Feed): LibriaCard = feedItem.run {
        when {
            release != null -> toCard(release!!)
            youtube != null -> toCard(youtube!!)
            else -> throw RuntimeException("WataFuq")
        }
    }

    fun Date.relativeDate(context: Context) = DateUtils.getRelativeDateTimeString(
        context,
        time,
        DateUtils.MINUTE_IN_MILLIS,
        DateUtils.DAY_IN_MILLIS * 2,
        DateUtils.FORMAT_SHOW_TIME
    ).toString()
}