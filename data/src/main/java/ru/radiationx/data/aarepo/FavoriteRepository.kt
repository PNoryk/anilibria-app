package ru.radiationx.data.aarepo

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.combiner.FavoriteCacheCombiner
import anilibria.tv.cache.combiner.ReleaseCacheCombiner
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.api.FavoriteApiDataSource
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteRepository(
    private val apiDataSource: FavoriteApiDataSource,
    private val cacheCombiner: FavoriteCacheCombiner,
    private val releaseCache: ReleaseCacheCombiner
) {

    fun observeList(): Observable<List<Release>> = cacheCombiner.observeList()

    fun getList(page: Int): Single<List<Release>> = apiDataSource
        .getList(page)
        .flatMap {
            if (page == 1) {
                cacheCombiner.clear().toSingleDefault(it)
            } else {
                cacheCombiner.putList(it.items).toSingleDefault(it)
            }
        }
        .flatMap { cacheCombiner.getList() }

    fun add(releaseId: Int): Single<Release> = apiDataSource
        .add(releaseId)
        .flatMap { cacheCombiner.putList(listOf(it)).toSingleDefault(it) }
        .flatMap { releaseCache.getOne(releaseId.toKey()) }

    fun delete(releaseId: Int): Single<Release> = apiDataSource
        .delete(releaseId)
        .flatMap {
            val removeFavorite = cacheCombiner.removeList(listOf(it.id.toKey()))
            val addRelease = releaseCache.putList(listOf(it))
            Completable.concat(listOf(removeFavorite, addRelease)).toSingleDefault(it)
        }
        .flatMap { releaseCache.getOne(releaseId.toKey()) }

    private fun Int.toKey() = ReleaseKey(this)
}