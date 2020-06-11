package anilibria.tv.cache.impl.combiner

import anilibria.tv.cache.FavoriteCache
import anilibria.tv.cache.combiner.FavoriteCacheCombiner
import anilibria.tv.cache.combiner.ReleaseCacheCombiner
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.converter.FavoriteRelativeConverter
import anilibria.tv.domain.entity.relative.FavoriteRelative
import anilibria.tv.domain.entity.release.Release
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteCacheCombinerImpl(
    private val favoriteCache: FavoriteCache,
    private val releaseCache: ReleaseCacheCombiner,
    private val relativeConverter: FavoriteRelativeConverter
) : FavoriteCacheCombiner {

    override fun observeList(): Observable<List<Release>> = favoriteCache
        .observeList()
        .switchMap { relativeItems ->
            releaseCache
                .observeSome(relativeItems.toKeys())
                .map(getSourceCombiner(relativeItems))
        }

    override fun getList(): Single<List<Release>> = favoriteCache
        .getList()
        .flatMap { relativeItems ->
            releaseCache
                .getSome(relativeItems.toKeys())
                .map(getSourceCombiner(relativeItems))
        }

    override fun insert(items: List<Release>): Completable = Completable.defer {
        val putRelease = releaseCache.insert(items)
        val putFavorite = favoriteCache.insert(items.map { relativeConverter.toRelative(it) })
        Completable.concat(listOf(putRelease, putFavorite))
    }

    override fun remove(keys: List<ReleaseKey>): Completable = favoriteCache.remove(keys)

    override fun clear(): Completable = favoriteCache.clear()

    private fun getSourceCombiner(relativeItems: List<FavoriteRelative>) = Function<List<Release>, List<Release>> { releaseItems ->
        relativeItems.mapNotNull { relative ->
            relativeConverter.fromRelative(relative, releaseItems)
        }
    }

    private fun List<FavoriteRelative>.toKeys() = map { ReleaseKey(it.releaseId) }
}