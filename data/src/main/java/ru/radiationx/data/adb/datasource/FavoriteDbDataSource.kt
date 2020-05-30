package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.FavoriteDao
import ru.radiationx.data.adb.converters.FavoriteConverter
import ru.radiationx.data.adomain.entity.relative.FavoriteRelative
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteDbDataSource(
    private val dao: FavoriteDao,
    private val converter: FavoriteConverter
) {

    fun getListAll(): Single<List<FavoriteRelative>> = dao
        .getList()
        .map(converter::toDomain)

    fun getList(ids: List<Int>): Single<List<FavoriteRelative>> = dao
        .getList(ids)
        .map(converter::toDomain)

    fun getOne(releaseId: Int): Single<FavoriteRelative> = dao
        .getOne(releaseId)
        .map(converter::toDomain)

    fun insert(items: List<FavoriteRelative>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    fun removeList(ids: List<Int>): Completable = dao.delete(ids)

    fun deleteAll(): Completable = dao.deleteAll()
}