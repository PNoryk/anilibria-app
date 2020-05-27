package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.EpisodeDao
import ru.radiationx.data.adb.datasource.converters.EpisodeConverter
import ru.radiationx.data.adomain.release.Episode
import toothpick.InjectConstructor

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

    fun getOne(releaseId: Int, episodeId: Int): Single<Episode> = episodeDao
        .getOne(releaseId, episodeId)
        .map(episodeConverter::toDomain)

    fun insert(items: List<Pair<Int, Episode>>): Completable = Single.just(items)
        .map(episodeConverter::toDb)
        .flatMapCompletable(episodeDao::insert)

    fun delete(): Completable = episodeDao.deleteAll()
}