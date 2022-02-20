package ru.radiationx.anilibria.common

import android.content.Context
import android.text.Html
import toothpick.InjectConstructor
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.module.domain.entity.release.ReleaseStatus
import tv.anilibria.module.domain.entity.schedule.ScheduleDay
import java.text.NumberFormat
import java.util.*

@InjectConstructor
class DetailDataConverter(
    private val context: Context
) {

    fun toDetail(releaseItem: Release): LibriaDetails = releaseItem.run {
        LibriaDetails(
            id,
            title.orEmpty(),
            titleEng.orEmpty(),
            listOf(
                genres.firstOrNull()?.capitalize()?.trim(),
                "${seasons.firstOrNull()} год",
                types.firstOrNull()?.trim(),
                "Серии: ${series?.trim() ?: "Не доступно"}"
            ).joinToString(" • "),
            Html.fromHtml(description.orEmpty()).toString().trim().trim('"')/*.replace('\n', ' ')*/,
            getAnnounce(),
            poster.orEmpty(),
            NumberFormat.getNumberInstance().format(favoriteInfo.rating),
            (releaseItem as? Release)?.episodes?.any { it.urlFullHd != null } ?: false,
            favoriteInfo.isAdded,
            (releaseItem as? Release)?.episodes?.isNotEmpty() ?: false,
            (releaseItem as? Release)?.episodes?.any { it.isViewed } ?: false,
            false && (releaseItem as? Release)?.moonwalkLink != null
        )
    }

    fun Release.getAnnounce(): String {
        if (statusCode == ReleaseStatus.COMPLETE) {
            return "Релиз завершен"
        }

        val originalAnnounce = announce?.trim()?.trim('.')?.capitalize()
        val scheduleAnnounce = days.firstOrNull()?.toAnnounce2().orEmpty()
        return originalAnnounce ?: scheduleAnnounce
    }

    fun String.toAnnounce(): String {
        val calendarDay = ScheduleDay.toCalendarDay(this)
        val displayDay = Calendar.getInstance().let {
            it.set(Calendar.DAY_OF_WEEK, calendarDay)
            it.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        }.orEmpty()
        val prefix = calendarDay.dayIterationPrefix()
        return "Новая серия $prefix $displayDay"
    }

    fun String.toAnnounce2(): String {
        val calendarDay = ScheduleDay.toCalendarDay(this)
        val displayDay = Calendar.getInstance().let {
            it.set(Calendar.DAY_OF_WEEK, calendarDay)
            it.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        }.orEmpty()
        val prefix = calendarDay.dayIterationPrefix2()
        return "Серии выходят $prefix"
    }

    fun Int.dayIterationPrefix(): String = when (this) {
        Calendar.MONDAY,
        Calendar.TUESDAY,
        Calendar.THURSDAY -> "каждый"
        Calendar.WEDNESDAY,
        Calendar.FRIDAY,
        Calendar.SATURDAY -> "каждую"
        Calendar.SUNDAY -> "каждое"
        else -> throw Exception("Not found day by $this")
    }

    fun Int.dayIterationPrefix2(): String = when (this) {
        Calendar.MONDAY -> "в понедельник"
        Calendar.TUESDAY -> "во вторник"
        Calendar.WEDNESDAY -> "в среду"
        Calendar.THURSDAY -> "в четверг"
        Calendar.FRIDAY -> "в пятницу"
        Calendar.SATURDAY -> "в субботу"
        Calendar.SUNDAY -> "в воскресенье"
        else -> throw Exception("Not found day by $this")
    }
}