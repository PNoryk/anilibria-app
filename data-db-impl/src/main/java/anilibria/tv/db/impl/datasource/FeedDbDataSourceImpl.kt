package anilibria.tv.db.impl.datasource

import anilibria.tv.db.FeedDbDataSource
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.converters.FeedConverter
import anilibria.tv.db.impl.dao.FeedDao
import anilibria.tv.domain.entity.common.keys.FeedKey
import anilibria.tv.domain.entity.relative.FeedRelative
import toothpick.InjectConstructor

@InjectConstructor
class FeedDbDataSourceImpl(
    private val dao: FeedDao,
    private val converter: FeedConverter
) : FeedDbDataSource {

    override fun getList(): Single<List<FeedRelative>> = dao
        .getList()
        .map(converter::toDomain)

    override fun getSome(keys: List<FeedKey>): Single<List<FeedRelative>> = dao
        .getSome(converter.toDbKey(keys))
        .map(converter::toDomain)

    override fun getOne(key: FeedKey): Single<FeedRelative> = dao
        .getOne(converter.toDbKey(key))
        .map(converter::toDomain)

    override fun insert(items: List<FeedRelative>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable { dao.insert(it) }

    override fun remove(keys: List<FeedKey>): Completable = Completable
        .fromAction {
            if (keys.any { it.releaseId == null && it.youtubeId == null }) {
                throw IllegalArgumentException("All keys should contains not null ids")
            }
        }
        .andThen(dao.remove(converter.toDbKey(keys)))

    override fun clear(): Completable = dao.clear()
}