package ru.radiationx.data.acache.combiner.impl

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import ru.radiationx.data.acache.FavoriteCache
import ru.radiationx.data.acache.combiner.FavoriteCacheCombiner
import ru.radiationx.data.acache.combiner.ReleaseCacheCombiner
import ru.radiationx.data.adomain.entity.history.HistoryItem
import ru.radiationx.data.adomain.entity.relative.FavoriteRelative
import ru.radiationx.data.adomain.entity.relative.HistoryRelative
import ru.radiationx.data.adomain.entity.release.Release
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