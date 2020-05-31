package anilibria.tv.domain.entity.converter

import anilibria.tv.domain.entity.relative.ScheduleDayRelative
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.schedule.ScheduleDay
import toothpick.InjectConstructor

@InjectConstructor
class ScheduleDayRelativeConverter {

    fun toRelative(source: ScheduleDay) = ScheduleDayRelative(
        dayId = source.day,
        releaseIds = source.items.map { it.id }
    )

    fun fromRelativeFiltered(source: ScheduleDayRelative, releaseItems: List<Release>) = ScheduleDay(
        day = source.dayId,
        items = releaseItems
    )

    fun fromRelative(source: ScheduleDayRelative, releaseItems: List<Release>): ScheduleDay {
        val releases = source.releaseIds.mapNotNull { releaseId ->
            releaseItems.firstOrNull { it.id == releaseId }
        }
        return fromRelativeFiltered(source, releases)
    }
}