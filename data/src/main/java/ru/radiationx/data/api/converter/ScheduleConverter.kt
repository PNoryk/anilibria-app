package ru.radiationx.data.api.converter

import anilibria.tv.domain.entity.schedule.ScheduleDay
import ru.radiationx.data.api.entity.schedule.ScheduleDayResponse
import toothpick.InjectConstructor

@InjectConstructor
class ScheduleConverter(
    private val releaseConverter: ReleaseConverter,
    private val dayConverter: DayConverter
) {

    fun toDomain(response: ScheduleDayResponse) = ScheduleDay(
        day = dayConverter.toDomain(response.day),
        items = response.items.map { releaseConverter.toDomain(it) }
    )
}