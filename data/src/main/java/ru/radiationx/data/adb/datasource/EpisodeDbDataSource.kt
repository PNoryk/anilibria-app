package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.converters.EpisodeConverter
import ru.radiationx.data.adb.dao.EpisodeDao
import anilibria.tv.domain.entity.release.Episode
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeDbDataSource(
    private val dao: EpisodeDao,
    private val converter: EpisodeConverter
) {

    fun getListAll(): Single<List<Episode>> = dao
        .getListAll()
        .map(converter::toDomain)

    fun getList(releaseIds: List<Int>): Single<List<Episode>> = dao
        .getList(releaseIds)
        .map(converter::toDomain)

    fun getListByPairIds(ids: List<Pair<Int, Int>>): Single<List<Episode>> = dao
        .getListByKeys(converter.toDbKey(ids))
        .map(converter::toDomain)

    fun getOne(releaseId: Int, episodeId: Int): Single<Episode> = dao
        .getOne(releaseId, episodeId)
        .map(converter::toDomain)

    fun insert(items: List<Episode>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    fun removeList(ids: List<Pair<Int, Int>>): Completable = dao.delete(converter.toDbKey(ids))

    fun deleteAll(): Completable = dao.deleteAll()
}