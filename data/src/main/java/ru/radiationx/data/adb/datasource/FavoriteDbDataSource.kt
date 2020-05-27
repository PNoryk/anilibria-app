package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.EpisodeDao
import ru.radiationx.data.adb.dao.FavoriteDao
import ru.radiationx.data.adb.datasource.converters.EpisodeConverter
import ru.radiationx.data.adb.datasource.converters.FavoriteConverter
import ru.radiationx.data.adomain.relative.FavoriteRelative
import ru.radiationx.data.adomain.release.Episode
import ru.radiationx.data.adomain.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteDbDataSource(
    private val favoriteDao: FavoriteDao,
    private val favoriteConverter: FavoriteConverter
) {

    fun getListAll(): Single<List<FavoriteRelative>> = favoriteDao
        .getList()
        .map(favoriteConverter::toDomain)

    fun getOne(releaseId: Int): Single<FavoriteRelative> = favoriteDao
        .getOne(releaseId)
        .map(favoriteConverter::toDomain)

    fun insert(items: List<FavoriteRelative>): Completable = Single.just(items)
        .map(favoriteConverter::toDb)
        .flatMapCompletable(favoriteDao::insert)

    fun delete(items: List<FavoriteRelative>): Completable = Single.just(items)
        .map(favoriteConverter::toDb)
        .flatMapCompletable(favoriteDao::delete)
}