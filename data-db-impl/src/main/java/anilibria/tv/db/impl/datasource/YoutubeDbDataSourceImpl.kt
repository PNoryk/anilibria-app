package anilibria.tv.db.impl.datasource

import anilibria.tv.db.YoutubeDbDataSource
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.converters.YoutubeConverter
import anilibria.tv.db.impl.dao.YoutubeDao
import anilibria.tv.domain.entity.youtube.Youtube
import toothpick.InjectConstructor

@InjectConstructor
class YoutubeDbDataSourceImpl(
    private val dao: YoutubeDao,
    private val converter: YoutubeConverter
) : YoutubeDbDataSource {

    override fun getListAll(): Single<List<Youtube>> = dao
        .getListAll()
        .map(converter::toDomain)

    override fun getList(ids: List<Int>): Single<List<Youtube>> = dao
        .getList(ids)
        .map(converter::toDomain)

    override fun getOne(youtubeId: Int): Single<Youtube> = dao
        .getOne(youtubeId)
        .map(converter::toDomain)

    override fun insert(items: List<Youtube>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    override fun removeList(ids: List<Int>): Completable = dao.delete(ids)

    override fun deleteAll(): Completable = dao.deleteAll()
}