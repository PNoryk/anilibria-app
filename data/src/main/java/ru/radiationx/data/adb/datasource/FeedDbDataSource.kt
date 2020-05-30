package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.converters.FeedConverter
import ru.radiationx.data.adb.dao.FeedDao
import ru.radiationx.data.adomain.entity.relative.FeedRelative
import toothpick.InjectConstructor

@InjectConstructor
class FeedDbDataSource(
    private val dao: FeedDao,
    private val converter: FeedConverter
) {

    fun getListAll(): Single<List<FeedRelative>> = dao
        .getList()
        .map(converter::toDomain)

    fun getList(ids: List<Pair<Int?, Int?>>): Single<List<FeedRelative>> = dao
        .getList(converter.toDbKey(ids))
        .map(converter::toDomain)

    fun getOne(releaseId: Int?, youtubeId: Int?): Single<FeedRelative> = dao
        .getOne(releaseId, youtubeId)
        .map(converter::toDomain)

    fun insert(items: List<FeedRelative>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable { dao.insert(it) }

    fun removeList(ids: List<Pair<Int?, Int?>>): Completable = dao.delete(converter.toDbKey(ids))

    fun deleteAll(): Completable = dao.deleteAll()
}