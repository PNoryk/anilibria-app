package anilibria.tv.cache.impl.combiner

import anilibria.tv.cache.FeedCache
import anilibria.tv.cache.YoutubeCache
import anilibria.tv.cache.combiner.FeedCacheCombiner
import anilibria.tv.cache.combiner.ReleaseCacheCombiner
import anilibria.tv.domain.entity.common.keys.FeedKey
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.common.keys.YoutubeKey
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
            Observable.zip(
                releaseCache.observeSome(relativeItems.toReleaseKeys()),
                youtubeCache.observeSome(relativeItems.toYoutubeKeys()),
                getSourceCombiner(relativeItems)
            )
        }

    override fun getList(): Single<List<Feed>> = feedCache
        .getList()
        .flatMap { relativeItems ->
            Single.zip(
                releaseCache.getSome(relativeItems.toReleaseKeys()),
                youtubeCache.getSome(relativeItems.toYoutubeKeys()),
                getSourceCombiner(relativeItems)
            )
        }

    override fun putList(items: List<Feed>): Completable {
        val putRelease = releaseCache.putList(items.mapNotNull { it.release })
        val putYoutube = youtubeCache.putList(items.mapNotNull { it.youtube })
        val putFeed = feedCache.putList(items.map { relativeConverter.toRelative(it) })
        return Completable.concat(listOf(putRelease, putYoutube, putFeed))
    }

    override fun removeList(keys: List<FeedKey>): Completable = feedCache.removeList(keys)

    override fun clear(): Completable = feedCache.clear()

    private fun getSourceCombiner(relativeItems: List<FeedRelative>) =
        BiFunction<List<Release>, List<Youtube>, List<Feed>> { releaseItems, youtubeItems ->
            relativeItems.mapNotNull { relative ->
                relativeConverter.fromRelative(relative, releaseItems, youtubeItems)
            }
        }

    private fun List<FeedRelative>.toReleaseKeys() = mapNotNull { feed -> feed.releaseId?.let { ReleaseKey(it) } }

    private fun List<FeedRelative>.toYoutubeKeys() = mapNotNull { feed -> feed.youtubeId?.let { YoutubeKey(it) } }
}