package anilibria.tv.cache.impl.combiner

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import anilibria.tv.cache.FavoriteCache
import anilibria.tv.cache.combiner.FavoriteCacheCombiner
import anilibria.tv.cache.combiner.ReleaseCacheCombiner
import anilibria.tv.domain.entity.relative.FavoriteRelative
import anilibria.tv.domain.entity.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteCacheCombinerImpl(
    private val favoriteCache: FavoriteCache,
    private val releaseCache: ReleaseCacheCombiner
) : FavoriteCacheCombiner {

    override fun observeList(): Observable<List<Release>> = favoriteCache
        .observeList()
        .switchMap { relativeItems ->
            releaseCache
                .observeList(relativeItems.map { it.releaseId })
                .map(getSourceCombiner(relativeItems))
        }

    override fun getList(): Single<List<Release>> = favoriteCache
        .getList()
        .flatMap { relativeItems ->
            releaseCache
                .getList(relativeItems.map { it.releaseId })
                .map(getSourceCombiner(relativeItems))
        }

    override fun putList(items: List<Release>): Completable {
        val putRelease = releaseCache.putList(items)
        val putFavorite = favoriteCache.putList(items.map { FavoriteRelative(it.id) })
        return Completable.concat(listOf(putRelease, putFavorite))
    }

    override fun removeList(items: List<Release>): Completable = favoriteCache
        .removeList(items.map { FavoriteRelative(it.id) })

    override fun clear(): Completable = favoriteCache.clear()

    private fun getSourceCombiner(relativeItems: List<FavoriteRelative>) = Function<List<Release>, List<Release>> { releaseItems ->
        relativeItems.mapNotNull { relative ->
            releaseItems.firstOrNull { it.id == relative.releaseId }
        }
    }
}