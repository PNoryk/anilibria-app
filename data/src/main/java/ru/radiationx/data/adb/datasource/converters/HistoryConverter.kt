package ru.radiationx.data.adb.datasource.converters

import ru.radiationx.data.adb.history.FlatHistoryDb
import ru.radiationx.data.adb.history.HistoryDb
import ru.radiationx.data.adomain.history.HistoryItem
import toothpick.InjectConstructor

@InjectConstructor
class HistoryConverter(
    private val releaseConverter: ReleaseConverter
) {

    fun toDomain(historyDb: HistoryDb) = HistoryItem(
        timestamp = historyDb.history.timestamp,
        release = releaseConverter.toDomain(historyDb.release)
    )

    fun toDb(historyItem: HistoryItem) = HistoryDb(
        FlatHistoryDb(historyItem.release.id, historyItem.timestamp),
        releaseConverter.toDb(historyItem.release)
    )

    fun toDomain(items: List<HistoryDb>) = items.map { toDomain(it) }

    fun toDb(items: List<HistoryItem>) = items.map { toDb(it) }
}