package anilibria.tv.db.impl.datasource

import anilibria.tv.db.ReleaseHistoryDbDataSource
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.converters.ReleaseHistoryConverter
import anilibria.tv.db.impl.dao.ReleaseHistoryDao
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseHistoryDbDataSourceImpl(
    private val dao: ReleaseHistoryDao,
    private val converter: ReleaseHistoryConverter
) : ReleaseHistoryDbDataSource {

    override fun getListAll(): Single<List<ReleaseHistoryRelative>> = dao
        .getList()
        .map(converter::toDomain)

    override fun getList(ids: List<Int>): Single<List<ReleaseHistoryRelative>> = dao
        .getList(ids)
        .map(converter::toDomain)

    override fun getOne(releaseId: Int): Single<ReleaseHistoryRelative> = dao
        .getOne(releaseId)
        .map(converter::toDomain)

    override fun insert(items: List<ReleaseHistoryRelative>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable { dao.insert(it) }

    override fun removeList(ids: List<Int>): Completable = dao.delete(ids)

    override fun deleteAll(): Completable = dao.deleteAll()
}