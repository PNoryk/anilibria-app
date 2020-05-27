package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.HistoryDao
import ru.radiationx.data.adb.dao.ReleaseDao
import ru.radiationx.data.adb.datasource.converters.HistoryConverter
import ru.radiationx.data.adomain.history.HistoryItem
import ru.radiationx.data.adomain.relative.HistoryRelative
import ru.radiationx.data.adomain.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class HistoryDbDataSource(
    private val historyDao: HistoryDao,
    private val historyConverter: HistoryConverter
) {

    fun getListAll(): Single<List<HistoryRelative>> = historyDao
        .getList()
        .map(historyConverter::toDomain)

    fun getOne(releaseId: Int): Single<HistoryRelative> = historyDao
        .getOne(releaseId)
        .map(historyConverter::toDomain)

    fun insert(items: List<HistoryRelative>): Completable = Single.just(items)
        .map(historyConverter::toDb)
        .flatMapCompletable { historyDao.insert(it) }

    fun deleteAll(): Completable = historyDao.deleteAll()
}