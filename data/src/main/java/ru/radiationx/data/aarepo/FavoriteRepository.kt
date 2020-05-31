package ru.radiationx.data.aarepo

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.combiner.FavoriteCacheCombiner
import ru.radiationx.data.acache.combiner.ReleaseCacheCombiner
import ru.radiationx.data.adomain.entity.release.Release
import ru.radiationx.data.api.datasource.FavoriteApiDataSource
import ru.radiationx.data.api.datasource.impl.FavoriteApiDataSourceImpl
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
        .flatMap { releaseCache.getOne(releaseId) }

    fun delete(releaseId: Int): Single<Release> = apiDataSource
        .delete(releaseId)
        .flatMap {
            val removeFavorite = cacheCombiner.removeList(listOf(it))
            val addRelease = releaseCache.putList(listOf(it))
            Completable.concat(listOf(removeFavorite, addRelease)).toSingleDefault(it)
        }
        .flatMap { releaseCache.getOne(releaseId) }
}