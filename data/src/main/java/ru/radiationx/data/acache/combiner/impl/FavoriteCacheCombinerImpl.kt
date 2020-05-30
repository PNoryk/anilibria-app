package ru.radiationx.data.acache.combiner.impl

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.radiationx.data.acache.FavoriteCache
import ru.radiationx.data.acache.combiner.FavoriteCacheCombiner
import ru.radiationx.data.acache.combiner.ReleaseCacheCombiner
import ru.radiationx.data.adomain.entity.relative.FavoriteRelative
import ru.radiationx.data.adomain.entity.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteCacheCombinerImpl(
    private val favoriteCache: FavoriteCache,
    private val releaseCache: ReleaseCacheCombiner
) : FavoriteCacheCombiner {

    private val combiner by lazy {
        BiFunction<List<FavoriteRelative>, List<Release>, List<Release>> { relativeItems, releaseItems ->
            relativeItems.mapNotNull { relative ->
                releaseItems.firstOrNull { it.id == relative.releaseId }
            }
        }
    }

    override fun observeList(): Observable<List<Release>> = Observable
        .combineLatest(
            favoriteCache.observeList(),
            releaseCache.observeList(),
            combiner
        )

    override fun getList(): Single<List<Release>> = Single.zip(
        favoriteCache.getList(),
        releaseCache.getList(),
        combiner
    )

    override fun putList(items: List<Release>): Completable {
        val putRelease = releaseCache.putList(items)
        val putFavorite = favoriteCache.putList(items.map { FavoriteRelative(it.id) })
        return Completable.concat(listOf(putRelease, putFavorite))
    }

    override fun removeList(items: List<Release>): Completable = favoriteCache
        .removeList(items.map { FavoriteRelative(it.id) })

    override fun clear(): Completable = favoriteCache.clear()
}