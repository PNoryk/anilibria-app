package anilibria.tv.cache.impl.combiner

import anilibria.tv.cache.FeedCache
import anilibria.tv.cache.YoutubeCache
import anilibria.tv.cache.combiner.FeedCacheCombiner
import anilibria.tv.cache.combiner.ReleaseCacheCombiner
import anilibria.tv.domain.entity.converter.FeedRelativeConverter
import anilibria.tv.domain.entity.feed.Feed
import anilibria.tv.domain.entity.relative.FeedRelative
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.youtube.Youtube
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import toothpick.InjectConstructor

@InjectConstructor
class FeedCacheCombinerImpl(
    private val feedCache: FeedCache,
    private val youtubeCache: YoutubeCache,
    private val releaseCache: ReleaseCacheCombiner,
    private val relativeConverter: FeedRelativeConverter
) : FeedCacheCombiner {

    override fun observeList(): Observable<List<Feed>> = feedCache
        .observeList()
        .switchMap { relativeItems ->
            val releaseIds = relativeItems.mapNotNull { it.releaseId }
            val youtubeIds = relativeItems.mapNotNull { it.youtubeId }
            Observable.zip(
                releaseCache.observeList(releaseIds),
                youtubeCache.observeList(youtubeIds),
                getSourceCombiner(relativeItems)
            )
        }

    override fun getList(): Single<List<Feed>> = feedCache
        .getList()
        .flatMap { relativeItems ->
            val releaseIds = relativeItems.mapNotNull { it.releaseId }
            val youtubeIds = relativeItems.mapNotNull { it.youtubeId }
            Single.zip(
                releaseCache.getList(releaseIds),
                youtubeCache.getList(youtubeIds),
                getSourceCombiner(relativeItems)
            )
        }

    override fun putList(items: List<Feed>): Completable {
        val putRelease = releaseCache.putList(items.mapNotNull { it.release })
        val putYoutube = youtubeCache.putList(items.mapNotNull { it.youtube })
        val putFeed = feedCache.putList(items.map { relativeConverter.toRelative(it) })
        return Completable.concat(listOf(putRelease, putYoutube, putFeed))
    }

    override fun removeList(items: List<Feed>): Completable = feedCache
        .removeList(items.map { relativeConverter.toRelative(it) })

    override fun clear(): Completable = feedCache.clear()

    private fun getSourceCombiner(relativeItems: List<FeedRelative>) =
        BiFunction<List<Release>, List<Youtube>, List<Feed>> { releaseItems, youtubeItems ->
            relativeItems.mapNotNull { relative ->
                relativeConverter.fromRelative(relative, releaseItems, youtubeItems)
            }
        }
}