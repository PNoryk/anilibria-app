package anilibria.tv.db.impl.datasource

import anilibria.tv.db.FeedDbDataSource
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.converters.FeedConverter
import anilibria.tv.db.impl.dao.FeedDao
import anilibria.tv.domain.entity.relative.FeedRelative
import toothpick.InjectConstructor

@InjectConstructor
class FeedDbDataSourceImpl(
    private val dao: FeedDao,
    private val converter: FeedConverter
) : FeedDbDataSource {

    override fun getListAll(): Single<List<FeedRelative>> = dao
        .getList()
        .map(converter::toDomain)

    override fun getList(ids: List<Pair<Int?, Int?>>): Single<List<FeedRelative>> = dao
        .getList(converter.toDbKey(ids))
        .map(converter::toDomain)

    override fun getOne(releaseId: Int?, youtubeId: Int?): Single<FeedRelative> = dao
        .getOne(releaseId, youtubeId)
        .map(converter::toDomain)

    override fun insert(items: List<FeedRelative>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable { dao.insert(it) }

    override fun removeList(ids: List<Pair<Int?, Int?>>): Completable = dao.delete(converter.toDbKey(ids))

    override fun deleteAll(): Completable = dao.deleteAll()
}