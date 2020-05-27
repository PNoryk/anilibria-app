package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.HistoryDao
import ru.radiationx.data.adb.converters.HistoryConverter
import ru.radiationx.data.adomain.entity.relative.HistoryRelative
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