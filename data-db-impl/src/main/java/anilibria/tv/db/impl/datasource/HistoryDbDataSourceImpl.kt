package anilibria.tv.db.impl.datasource

import anilibria.tv.db.HistoryDbDataSource
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.converters.HistoryConverter
import anilibria.tv.db.impl.dao.HistoryDao
import anilibria.tv.domain.entity.relative.HistoryRelative
import toothpick.InjectConstructor

@InjectConstructor
class HistoryDbDataSourceImpl(
    private val dao: HistoryDao,
    private val converter: HistoryConverter
) : HistoryDbDataSource {

    override fun getListAll(): Single<List<HistoryRelative>> = dao
        .getList()
        .map(converter::toDomain)

    override fun getList(ids: List<Int>): Single<List<HistoryRelative>> = dao
        .getList(ids)
        .map(converter::toDomain)

    override fun getOne(releaseId: Int): Single<HistoryRelative> = dao
        .getOne(releaseId)
        .map(converter::toDomain)

    override fun insert(items: List<HistoryRelative>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable { dao.insert(it) }

    override fun removeList(ids: List<Int>): Completable = dao.delete(ids)

    override fun deleteAll(): Completable = dao.deleteAll()
}