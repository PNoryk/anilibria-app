package ru.radiationx.data.aarepo

import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.combiner.FeedCacheCombiner
import anilibria.tv.domain.entity.feed.Feed
import anilibria.tv.api.FeedApiDataSource
import toothpick.InjectConstructor

@InjectConstructor
class FeedRepository(
    private val apiDataSource: FeedApiDataSource,
    private val cacheCombiner: FeedCacheCombiner
) {

    fun observeList(): Observable<List<Feed>> = cacheCombiner.observeList()

    fun getList(page: Int): Single<List<Feed>> = apiDataSource
        .getList(page)
        .flatMap {
            if (page == 1) {
                cacheCombiner.clear().toSingleDefault(it)
            } else {
                cacheCombiner.putList(it.items).toSingleDefault(it)
            }
        }
        .flatMap { cacheCombiner.getList() }

}