package anilibria.tv.db.impl.datasource

import anilibria.tv.db.ReleaseHistoryDbDataSource
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.converters.ReleaseHistoryConverter
import anilibria.tv.db.impl.dao.ReleaseHistoryDao
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseHistoryDbDataSourceImpl(
    private val dao: ReleaseHistoryDao,
    private val converter: ReleaseHistoryConverter
) : ReleaseHistoryDbDataSource {

    override fun getList(): Single<List<ReleaseHistoryRelative>> = dao
        .getList()
        .map(converter::toDomain)

    override fun getSome(keys: List<ReleaseKey>): Single<List<ReleaseHistoryRelative>> = dao
        .getSome(keys.map { it.id })
        .map(converter::toDomain)

    override fun getOne(key: ReleaseKey): Single<ReleaseHistoryRelative> = dao
        .getOne(key.id)
        .map(converter::toDomain)

    override fun insert(items: List<ReleaseHistoryRelative>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable { dao.insert(it) }

    override fun remove(keys: List<ReleaseKey>): Completable = dao.remove(keys.map { it.id })

    override fun clear(): Completable = dao.clear()
}