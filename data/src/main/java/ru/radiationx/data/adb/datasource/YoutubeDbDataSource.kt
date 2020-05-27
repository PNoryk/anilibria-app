package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.YoutubeDao
import ru.radiationx.data.adb.converters.YoutubeConverter
import ru.radiationx.data.adomain.entity.youtube.Youtube
import toothpick.InjectConstructor

@InjectConstructor
class YoutubeDbDataSource(
    private val releaseDao: YoutubeDao,
    private val releaseConverter: YoutubeConverter
) {

    fun getListAll(): Single<List<Youtube>> = releaseDao
        .getListAll()
        .map(releaseConverter::toDomain)

    fun getOne(releaseId: Int): Single<Youtube> = releaseDao
        .getOne(releaseId)
        .map(releaseConverter::toDomain)

    fun insert(items: List<Youtube>): Completable = Single.just(items)
        .map(releaseConverter::toDb)
        .flatMapCompletable(releaseDao::insert)

    fun delete(): Completable = releaseDao.deleteAll()
}