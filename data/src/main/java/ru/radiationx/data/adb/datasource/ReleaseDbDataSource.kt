package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.ReleaseDao
import ru.radiationx.data.adb.converters.ReleaseConverter
import ru.radiationx.data.adomain.entity.relative.HistoryRelative
import ru.radiationx.data.adomain.entity.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseDbDataSource(
    private val dao: ReleaseDao,
    private val converter: ReleaseConverter
) {

    fun getListAll(): Single<List<Release>> = dao
        .getListAll()
        .map(converter::toDomain)

    fun getList(ids: List<Int>): Single<List<Release>> = dao
        .getList(ids)
        .map(converter::toDomain)

    fun getOne(releaseId: Int): Single<Release> = dao
        .getOne(releaseId)
        .map(converter::toDomain)

    fun insert(items: List<Release>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    fun removeList(ids: List<Int>): Completable = dao.delete(ids)

    fun delete(): Completable = dao.deleteAll()
}