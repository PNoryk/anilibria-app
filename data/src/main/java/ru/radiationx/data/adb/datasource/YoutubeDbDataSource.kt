package ru.radiationx.data.adb.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adb.dao.YoutubeDao
import ru.radiationx.data.adb.converters.YoutubeConverter
import ru.radiationx.data.adomain.entity.relative.ScheduleDayRelative
import ru.radiationx.data.adomain.entity.youtube.Youtube
import toothpick.InjectConstructor

@InjectConstructor
class YoutubeDbDataSource(
    private val dao: YoutubeDao,
    private val converter: YoutubeConverter
) {

    fun getListAll(): Single<List<Youtube>> = dao
        .getListAll()
        .map(converter::toDomain)

    fun getList(ids: List<Int>): Single<List<Youtube>> = dao
        .getList(ids)
        .map(converter::toDomain)

    fun getOne(youtubeId: Int): Single<Youtube> = dao
        .getOne(youtubeId)
        .map(converter::toDomain)

    fun insert(items: List<Youtube>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    fun removeList(ids: List<Int>): Completable = dao.delete(ids)

    fun deleteAll(): Completable = dao.deleteAll()
}