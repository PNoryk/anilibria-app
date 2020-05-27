package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.ReleaseDao
import ru.radiationx.data.adb.datasource.converters.ReleaseConverter
import ru.radiationx.data.adomain.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseDbDataSource(
    private val releaseDao: ReleaseDao,
    private val releaseConverter: ReleaseConverter
) {

    fun getListAll(): Single<List<Release>> = releaseDao
        .getListAll()
        .map(releaseConverter::toDomain)

    fun getOne(releaseId: Int): Single<Release> = releaseDao
        .getOne(releaseId)
        .map(releaseConverter::toDomain)

    fun insert(items: List<Release>): Completable = Single.just(items)
        .map(releaseConverter::toDb)
        .flatMapCompletable(releaseDao::insert)

    fun delete(): Completable = releaseDao.deleteAll()
}