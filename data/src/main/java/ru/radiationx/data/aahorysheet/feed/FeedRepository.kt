package ru.radiationx.data.aahorysheet.feed

import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.adomain.feed.FeedItem

class FeedRepository(
    private val cache: FakeFeedCache,
    private val remote: FakeFeedRemote,
    private val fakeFeedCacheCombiner: FakeFeedCacheCombiner
) {

    fun observeFeed(): Observable<List<FeedItem>> = fakeFeedCacheCombiner.observeList()

    fun getList(page: Int): Single<List<FeedItem>> = remote
        .getList()
        .flatMap {
            if (page == 1) {
                fakeFeedCacheCombiner.clear().toSingleDefault(it)
            } else {
                fakeFeedCacheCombiner.putItems(it).toSingleDefault(it)
            }
        }
        .flatMap { fakeFeedCacheCombiner.fetchList() }


}