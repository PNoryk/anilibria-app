package ru.radiationx.data.api.converter

import ru.radiationx.data.adomain.ScheduleDay
import ru.radiationx.data.api.remote.ScheduleDayResponse
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