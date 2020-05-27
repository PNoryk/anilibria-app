package ru.radiationx.data.aahorysheet.feed

import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.adomain.feed.Feed

class FeedRepository(
    private val cache: FakeFeedCache,
    private val remote: FakeFeedRemote,
    private val fakeFeedCacheCombiner: FakeFeedCacheCombiner
) {

    fun observeFeed(): Observable<List<Feed>> = fakeFeedCacheCombiner.observeList()

    fun getList(page: Int): Single<List<Feed>> = remote
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