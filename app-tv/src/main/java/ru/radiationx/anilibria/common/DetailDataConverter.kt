package ru.radiationx.anilibria.common

import android.text.Html
import kotlinx.datetime.DayOfWeek
import toothpick.InjectConstructor
import tv.anilibria.module.data.UrlHelper
import tv.anilibria.module.domain.entity.EpisodeVisit
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.module.domain.entity.release.ReleaseStatus
import java.text.NumberFormat

@InjectConstructor
class DetailDataConverter(
    private val urlHelper: UrlHelper
) {

    fun toDetail(
        releaseItem: Release,
        visits: List<EpisodeVisit>
    ): LibriaDetails = releaseItem.run {
        LibriaDetails(
            id = id,
            titleRu = nameRus?.text.orEmpty(),
            titleEn = nameEng?.text.orEmpty(),
            extra = listOf(
                genres?.firstOrNull()?.value?.capitalize()?.trim(),
                "${year?.value} год",
                type?.trim(),
                "Серии: ${series?.trim() ?: "Не доступно"}"
            ).joinToString(" • "),
            description = Html.fromHtml(description?.text.orEmpty()).toString().trim()
                .trim('"')/*.replace('\n', ' ')*/,
            announce = getAnnounce(),
            image = urlHelper.makeMedia(poster),
            favoriteCount = favoriteInfo?.rating?.value?.let {
                NumberFormat.getNumberInstance().format(it)
            },
            hasFullHd = (releaseItem as? Release)?.episodes?.any { it.urlFullHd != null } ?: false,
            isFavorite = favoriteInfo?.isAdded ?: false,
            hasEpisodes = releaseItem.episodes?.isNotEmpty() ?: false,
            hasViewed = visits.any { it.isViewed },
            hasWebPlayer = false
        )
    }

    fun Release.getAnnounce(): String {
        if (status == ReleaseStatus.COMPLETE) {
            return "Релиз завершен"
        }

        val originalAnnounce = announce?.text?.trim()?.trim('.')?.capitalize()
        val scheduleAnnounce = scheduleDay?.toAnnounce2().orEmpty()
        return originalAnnounce ?: scheduleAnnounce
    }

    fun DayOfWeek.toAnnounce2(): String {
        val prefix = dayIterationPrefix2()
        return "Серии выходят $prefix"
    }

    fun DayOfWeek.dayIterationPrefix2(): String = when (this) {
        DayOfWeek.MONDAY -> "в понедельник"
        DayOfWeek.TUESDAY -> "во вторник"
        DayOfWeek.WEDNESDAY -> "в среду"
        DayOfWeek.THURSDAY -> "в четверг"
        DayOfWeek.FRIDAY -> "в пятницу"
        DayOfWeek.SATURDAY -> "в субботу"
        DayOfWeek.SUNDAY -> "в воскресенье"
    }
}