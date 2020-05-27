package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.FeedDao
import ru.radiationx.data.adb.converters.FeedConverter
import ru.radiationx.data.adomain.entity.relative.FeedRelative
import toothpick.InjectConstructor

@InjectConstructor
class FeedDbDataSource(
    private val feedDao: FeedDao,
    private val feedConverter: FeedConverter
) {

    fun getListAll(): Single<List<FeedRelative>> = feedDao
        .getList()
        .map(feedConverter::toDomain)

    fun getOne(feedId: Int): Single<FeedRelative> = feedDao
        .getOne(feedId)
        .map(feedConverter::toDomain)

    fun getOne(releaseId: Int?, youtubeId: Int?): Single<FeedRelative> = feedDao
        .getOne(releaseId, youtubeId)
        .map(feedConverter::toDomain)

    fun insert(items: List<FeedRelative>): Completable = Single.just(items)
        .map(feedConverter::toDb)
        .flatMapCompletable { feedDao.insert(it) }

    fun deleteAll(): Completable = feedDao.deleteAll()
}