package ru.radiationx.data.aahorysheet.feed

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function3
import ru.radiationx.data.aahorysheet.release.FakeReleaseCache
import ru.radiationx.data.aahorysheet.youtube.FakeYTCache
import ru.radiationx.data.adomain.feed.Feed
import ru.radiationx.data.adomain.release.Release
import ru.radiationx.data.adomain.youtube.Youtube

class FakeFeedCacheCombiner(
    private val feedCache: FakeFeedCache,
    private val releaseCache: FakeReleaseCache,
    private val ytCache: FakeYTCache
) {

    private val combiner by lazy {
        Function3<List<FeedRelative>, List<Release>, List<Youtube>, List<Feed>> { t1, t2, t3 ->
            return@Function3 t1.map { relative ->
                val release = relative.releaseId?.let { releaseId ->
                    t2.firstOrNull { it.id == releaseId }
                }
                val youtube = relative.youtubeId?.let { youtubeId ->
                    t3.firstOrNull { it.id == youtubeId }
                }
                Feed(release, youtube)
            }.filter {
                it.release == null || it.youtube != null
            }
        }
    }


    fun observeList(): Observable<List<Feed>> = Observable
        .combineLatest(
            feedCache.observeChanges(),
            releaseCache.observeChanges(),
            ytCache.observeChanges(),
            combiner
        )
        .distinctUntilChanged()

    fun fetchList(): Single<List<Feed>> = Single
        .zip(
            feedCache.getList(),
            releaseCache.getList(),
            ytCache.getList(),
            combiner
        )

    fun putItems(items: List<Feed>): Completable {
        val putRelease = releaseCache.putList(items.mapNotNull { it.release })
        val putYoutube = ytCache.putList(items.mapNotNull { it.youtube })
        val putFeed = feedCache.putList(items.map { FeedRelative(it.release?.id, it.youtube?.id) })
        return Completable.concat(listOf(putRelease, putYoutube, putFeed))
    }

    fun clear(): Completable = feedCache.clear()

}