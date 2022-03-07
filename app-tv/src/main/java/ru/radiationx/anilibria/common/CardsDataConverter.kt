package ru.radiationx.anilibria.common

import android.content.Context
import android.text.format.DateUtils
import tv.anilibria.feature.data.BaseUrlHelper
import tv.anilibria.feature.domain.entity.feed.Feed
import tv.anilibria.feature.domain.entity.release.Release
import tv.anilibria.feature.domain.entity.youtube.Youtube
import java.util.*

class CardsDataConverter(
    private val context: Context,
    private val urlHelper: BaseUrlHelper
) {

    fun toCard(releaseItem: Release): LibriaCard {
        val seasonText = releaseItem.year?.value
        val genreText = releaseItem.genres?.firstOrNull()?.value?.capitalize()
        val seriesText = releaseItem.series?.trim() ?: "Не доступно"
        val dateText = releaseItem.torrentUpdate
            ?.toEpochMilliseconds()
            ?.let { Date(it).relativeDate(context).decapitalize() }
        return releaseItem.run {
            LibriaCard(
                id.id,
                nameRus?.text.orEmpty(),
                "$seasonText год • $genreText • Серии: $seriesText • Обновлен $dateText",
                urlHelper.makeMedia(poster),
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
            urlHelper.makeMedia(youtubeItem.image),
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