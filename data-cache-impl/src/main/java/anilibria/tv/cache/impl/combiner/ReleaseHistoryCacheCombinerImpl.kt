package anilibria.tv.cache.impl.combiner

import anilibria.tv.cache.ReleaseHistoryCache
import anilibria.tv.cache.combiner.ReleaseHistoryCacheCombiner
import anilibria.tv.cache.combiner.ReleaseCacheCombiner
import anilibria.tv.domain.entity.converter.ReleaseHistoryRelativeConverter
import anilibria.tv.domain.entity.history.ReleaseHistory
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import anilibria.tv.domain.entity.release.Release
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseHistoryCacheCombinerImpl(
    private val releaseHistoryCache: ReleaseHistoryCache,
    private val releaseCache: ReleaseCacheCombiner,
    private val relativeConverter: ReleaseHistoryRelativeConverter
) : ReleaseHistoryCacheCombiner {

    override fun observeList(): Observable<List<ReleaseHistory>> = releaseHistoryCache
        .observeList()
        .switchMap { relativeItems ->
            releaseCache
                .observeList(relativeItems.map { it.releaseId })
                .map(getSourceCombiner(relativeItems))
        }

    override fun getList(): Single<List<ReleaseHistory>> = releaseHistoryCache
        .getList()
        .flatMap { relativeItems ->
            releaseCache
                .getList(relativeItems.map { it.releaseId })
                .map(getSourceCombiner(relativeItems))
        }

    override fun putList(items: List<ReleaseHistory>): Completable {
        val putRelease = releaseCache.putList(items.map { it.release })
        val putFavorite = releaseHistoryCache.putList(items.map { relativeConverter.toRelative(it) })
        return Completable.concat(listOf(putRelease, putFavorite))
    }

    override fun removeList(items: List<ReleaseHistory>): Completable = releaseHistoryCache
        .removeList(items.map { relativeConverter.toRelative(it) })

    override fun clear(): Completable = releaseHistoryCache.clear()

    private fun getSourceCombiner(relativeItems: List<ReleaseHistoryRelative>) =
        Function<List<Release>, List<ReleaseHistory>> { releaseItems ->
            relativeItems.mapNotNull { relative ->
                relativeConverter.fromRelative(relative, releaseItems)
            }
        }
}