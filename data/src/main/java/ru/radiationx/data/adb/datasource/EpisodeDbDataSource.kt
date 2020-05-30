package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.Function
import ru.radiationx.data.adb.dao.EpisodeDao
import ru.radiationx.data.adb.converters.EpisodeConverter
import ru.radiationx.data.adomain.entity.release.Episode
import toothpick.InjectConstructor
import java.lang.Exception

@InjectConstructor
class EpisodeDbDataSource(
    private val episodeDao: EpisodeDao,
    private val episodeConverter: EpisodeConverter
) {

    fun getListAll(): Single<List<Episode>> = episodeDao
        .getListAll()
        .map(episodeConverter::toDomain)

    fun getList(releaseId: Int): Single<List<Episode>> = episodeDao
        .getList(releaseId)
        .map(episodeConverter::toDomain)

    fun getList(ids: List<Pair<Int, Int>>): Single<List<Episode>> = episodeDao
        .getList(episodeConverter.toDbKey(ids))
        .map(episodeConverter::toDomain)

    fun getOne(releaseId: Int, episodeId: Int): Single<Episode> = episodeDao
        .getOne(releaseId, episodeId)
        .map(episodeConverter::toDomain)

    fun insert(items: List<Episode>): Completable = Single.just(items)
        .map(episodeConverter::toDb)
        .flatMapCompletable(episodeDao::insert)

    fun removeList(ids: List<Pair<Int, Int>>): Completable = episodeDao.delete(episodeConverter.toDbKey(ids))

    fun delete(): Completable = episodeDao.deleteAll()
}