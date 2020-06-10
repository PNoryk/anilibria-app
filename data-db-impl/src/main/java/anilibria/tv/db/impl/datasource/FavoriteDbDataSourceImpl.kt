package anilibria.tv.db.impl.datasource

import anilibria.tv.db.FavoriteDbDataSource
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.converters.FavoriteConverter
import anilibria.tv.db.impl.dao.FavoriteDao
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.relative.FavoriteRelative
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteDbDataSourceImpl(
    private val dao: FavoriteDao,
    private val converter: FavoriteConverter
) : FavoriteDbDataSource {

    override fun getList(): Single<List<FavoriteRelative>> = dao
        .getSome()
        .map(converter::toDomain)

    override fun getSome(keys: List<ReleaseKey>): Single<List<FavoriteRelative>> = dao
        .getSome(keys.map { it.id })
        .map(converter::toDomain)

    override fun getOne(key: ReleaseKey): Single<FavoriteRelative> = dao
        .getOne(key.id)
        .map(converter::toDomain)

    override fun insert(items: List<FavoriteRelative>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    override fun remove(keys: List<ReleaseKey>): Completable = dao.remove(keys.map { it.id })

    override fun clear(): Completable = dao.clear()
}