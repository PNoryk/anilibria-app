package anilibria.tv.db.impl.datasource

import anilibria.tv.db.YoutubeDbDataSource
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.converters.YoutubeConverter
import anilibria.tv.db.impl.dao.YoutubeDao
import anilibria.tv.domain.entity.common.keys.YoutubeKey
import anilibria.tv.domain.entity.youtube.Youtube
import toothpick.InjectConstructor

@InjectConstructor
class YoutubeDbDataSourceImpl(
    private val dao: YoutubeDao,
    private val converter: YoutubeConverter
) : YoutubeDbDataSource {

    override fun getList(): Single<List<Youtube>> = dao
        .getList()
        .map(converter::toDomain)

    override fun getSome(keys: List<YoutubeKey>): Single<List<Youtube>> = dao
        .getSome(keys.map { it.id })
        .map(converter::toDomain)

    override fun getOne(key: YoutubeKey): Single<Youtube> = dao
        .getOne(key.id)
        .map(converter::toDomain)

    override fun insert(items: List<Youtube>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    override fun remove(keys: List<YoutubeKey>): Completable = dao.remove(keys.map { it.id })

    override fun clear(): Completable = dao.clear()
}