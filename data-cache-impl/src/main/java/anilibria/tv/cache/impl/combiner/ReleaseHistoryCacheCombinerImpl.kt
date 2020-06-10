package anilibria.tv.cache.impl.combiner

import anilibria.tv.cache.ReleaseHistoryCache
import anilibria.tv.cache.combiner.ReleaseHistoryCacheCombiner
import anilibria.tv.cache.combiner.ReleaseCacheCombiner
import anilibria.tv.domain.entity.common.keys.ReleaseKey
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
                .observeSome(relativeItems.toKeys())
                .map(getSourceCombiner(relativeItems))
        }

    override fun getList(): Single<List<ReleaseHistory>> = releaseHistoryCache
        .getList()
        .flatMap { relativeItems ->
            releaseCache
                .getSome(relativeItems.toKeys())
                .map(getSourceCombiner(relativeItems))
        }

    override fun putList(items: List<ReleaseHistory>): Completable {
        val putRelease = releaseCache.putList(items.map { it.release })
        val putFavorite = releaseHistoryCache.putList(items.map { relativeConverter.toRelative(it) })
        return Completable.concat(listOf(putRelease, putFavorite))
    }

    override fun removeList(keys: List<ReleaseKey>): Completable = releaseHistoryCache.removeList(keys)

    override fun clear(): Completable = releaseHistoryCache.clear()

    private fun getSourceCombiner(relativeItems: List<ReleaseHistoryRelative>) =
        Function<List<Release>, List<ReleaseHistory>> { releaseItems ->
            relativeItems.mapNotNull { relative ->
                relativeConverter.fromRelative(relative, releaseItems)
            }
        }

    private fun List<ReleaseHistoryRelative>.toKeys() = map { ReleaseKey(it.releaseId) }
}