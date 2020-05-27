package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.FeedDao
import ru.radiationx.data.adb.dao.ReleaseDao
import ru.radiationx.data.adb.dao.YoutubeDao
import ru.radiationx.data.adb.datasource.converters.FeedConverter
import ru.radiationx.data.adomain.feed.FeedItem
import toothpick.InjectConstructor

@InjectConstructor
class FeedDbDataSource(
    private val feedDao: FeedDao,
    private val releaseDao: ReleaseDao,
    private val youtubeDao: YoutubeDao,
    private val feedConverter: FeedConverter
) {

    fun getListAll(): Single<List<FeedItem>> = feedDao
        .getList()
        .map(feedConverter::toDomain)

    fun getOne(feedId: Int): Single<FeedItem> = feedDao
        .getOne(feedId)
        .map(feedConverter::toDomain)

    fun getOne(releaseId: Int?, youtubeId: Int?): Single<FeedItem> = feedDao
        .getOne(releaseId, youtubeId)
        .map(feedConverter::toDomain)

    fun insert(items: List<FeedItem>): Completable = Single.just(items)
        .map(feedConverter::toDb)
        .flatMapCompletable { feedDao.insert(it, releaseDao, youtubeDao) }

    fun deleteAll(): Completable = feedDao.deleteAll()
}