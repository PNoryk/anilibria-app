package anilibria.tv.db

import anilibria.tv.domain.entity.relative.HistoryRelative
import io.reactivex.Completable
import io.reactivex.Single

interface HistoryDbDataSource {
    fun getListAll(): Single<List<HistoryRelative>>
    fun getList(ids: List<Int>): Single<List<HistoryRelative>>
    fun getOne(releaseId: Int): Single<HistoryRelative>
    fun insert(items: List<HistoryRelative>): Completable
    fun removeList(ids: List<Int>): Completable
    fun deleteAll(): Completable
}