package ru.radiationx.data.adb.datasource

import anilibria.tv.db.FavoriteDbDataSource
import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.converters.FavoriteConverter
import ru.radiationx.data.adb.dao.FavoriteDao
import anilibria.tv.domain.entity.relative.FavoriteRelative
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteDbDataSourceImpl(
    private val dao: FavoriteDao,
    private val converter: FavoriteConverter
) : FavoriteDbDataSource {

    override fun getListAll(): Single<List<FavoriteRelative>> = dao
        .getList()
        .map(converter::toDomain)

    override fun getList(ids: List<Int>): Single<List<FavoriteRelative>> = dao
        .getList(ids)
        .map(converter::toDomain)

    override fun getOne(releaseId: Int): Single<FavoriteRelative> = dao
        .getOne(releaseId)
        .map(converter::toDomain)

    override fun insert(items: List<FavoriteRelative>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    override fun removeList(ids: List<Int>): Completable = dao.delete(ids)

    override fun deleteAll(): Completable = dao.deleteAll()
}