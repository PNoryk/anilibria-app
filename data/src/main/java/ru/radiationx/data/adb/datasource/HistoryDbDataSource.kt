package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.HistoryDao
import ru.radiationx.data.adb.converters.HistoryConverter
import ru.radiationx.data.adomain.entity.relative.FavoriteRelative
import ru.radiationx.data.adomain.entity.relative.HistoryRelative
import toothpick.InjectConstructor

@InjectConstructor
class HistoryDbDataSource(
    private val dao: HistoryDao,
    private val converter: HistoryConverter
) {

    fun getListAll(): Single<List<HistoryRelative>> = dao
        .getList()
        .map(converter::toDomain)

    fun getList(ids: List<Int>): Single<List<HistoryRelative>> = dao
        .getList(ids)
        .map(converter::toDomain)

    fun getOne(releaseId: Int): Single<HistoryRelative> = dao
        .getOne(releaseId)
        .map(converter::toDomain)

    fun insert(items: List<HistoryRelative>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable { dao.insert(it) }

    fun removeList(ids: List<Int>): Completable = dao.delete(ids)

    fun deleteAll(): Completable = dao.deleteAll()
}