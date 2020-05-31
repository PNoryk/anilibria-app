package ru.radiationx.data.aarepo

import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.combiner.FeedCacheCombiner
import ru.radiationx.data.adomain.entity.feed.Feed
import ru.radiationx.data.api.datasource.FeedApiDataSource
import ru.radiationx.data.api.datasource.impl.FeedApiDataSourceImpl
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