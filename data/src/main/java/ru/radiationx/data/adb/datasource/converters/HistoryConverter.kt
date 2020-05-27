package ru.radiationx.data.adb.datasource.converters

import ru.radiationx.data.adb.entity.history.HistoryDb
import ru.radiationx.data.adomain.relative.HistoryRelative
import toothpick.InjectConstructor

@InjectConstructor
class HistoryConverter {

    fun toDomain(source: HistoryDb) = HistoryRelative(
        releaseId = source.releaseId,
        timestamp = source.timestamp
    )

    fun toDb(source: HistoryRelative) = HistoryDb(
        releaseId = source.releaseId,
        timestamp = source.timestamp
    )

    fun toDomain(source: List<HistoryDb>) = source.map { toDomain(it) }

    fun toDb(source: List<HistoryRelative>) = source.map { toDb(it) }
}