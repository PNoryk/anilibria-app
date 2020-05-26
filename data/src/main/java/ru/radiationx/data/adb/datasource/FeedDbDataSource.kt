package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.FeedDao
import ru.radiationx.data.adb.datasource.converters.FeedConverter
import ru.radiationx.data.adomain.feed.FeedItem
import ru.radiationx.data.adomain.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class FeedDbDataSource(
    private val feedDao: FeedDao,
    private val feedConverter: FeedConverter
) {

    fun getListAll(): Single<List<FeedItem>> = feedDao
        .getList()
        .map(feedConverter::toDomain)

    fun getOne(releaseId: Int): Single<FeedItem> = feedDao
        .getOne(releaseId)
        .map(feedConverter::toDomain)
/*
    fun insert(items: List<FeedItem>): Completable = Single.just(items)
        .map(feedConverter::toDb)
        .flatMapCompletable(feedDao::insert)

    fun delete(items: List<FeedItem>): Completable = Single.just(items)
        .map(feedConverter::toDb)
        .flatMapCompletable(feedDao::delete)*/
}