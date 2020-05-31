package anilibria.tv.db

import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative
import io.reactivex.Completable
import io.reactivex.Single

interface ReleaseHistoryDbDataSource {
    fun getListAll(): Single<List<ReleaseHistoryRelative>>
    fun getList(ids: List<Int>): Single<List<ReleaseHistoryRelative>>
    fun getOne(releaseId: Int): Single<ReleaseHistoryRelative>
    fun insert(items: List<ReleaseHistoryRelative>): Completable
    fun removeList(ids: List<Int>): Completable
    fun deleteAll(): Completable
}