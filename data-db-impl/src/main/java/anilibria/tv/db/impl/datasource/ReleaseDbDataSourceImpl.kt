package anilibria.tv.db.impl.datasource

import anilibria.tv.db.ReleaseDbDataSource
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.converters.ReleaseConverter
import anilibria.tv.db.impl.dao.ReleaseDao
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseDbDataSourceImpl(
    private val dao: ReleaseDao,
    private val converter: ReleaseConverter
) : ReleaseDbDataSource {

    override fun getList(): Single<List<Release>> = dao
        .getList()
        .map(converter::toDomain)

    override fun getSome(keys: List<ReleaseKey>): Single<List<Release>> = dao
        .getSome(keys.map { it.id })
        .map(converter::toDomain)

    override fun getOne(key: ReleaseKey): Single<Release> = dao
        .getOne(key.id)
        .map(converter::toDomain)

    override fun insert(items: List<Release>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    override fun remove(keys: List<ReleaseKey>): Completable = dao.remove(keys.map { it.id })

    override fun clear(): Completable = dao.clear()

}