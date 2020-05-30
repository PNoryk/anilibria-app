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

    fun getList(ids: List<Int>?, codes: List<String>?): Single<List<Release>> = dao
        .getList(ids.orEmpty(), codes.orEmpty())
        .map(converter::toDomain)

    fun getOne(releaseId: Int?, code: String?): Single<Release> = dao
        .getOne(releaseId, code)
        .map(converter::toDomain)

    fun insert(items: List<Release>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    fun removeList(ids: List<Int>): Completable = dao.delete(ids)

    fun deleteAll(): Completable = dao.deleteAll()
}