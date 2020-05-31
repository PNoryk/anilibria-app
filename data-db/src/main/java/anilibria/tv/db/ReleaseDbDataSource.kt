package anilibria.tv.db

import anilibria.tv.domain.entity.release.Release
import io.reactivex.Completable
import io.reactivex.Single

interface ReleaseDbDataSource {
    fun getListAll(): Single<List<Release>>
    fun getList(ids: List<Int>?, codes: List<String>?): Single<List<Release>>
    fun getOne(releaseId: Int?, code: String?): Single<Release>
    fun insert(items: List<Release>): Completable
    fun removeList(ids: List<Int>): Completable
    fun deleteAll(): Completable
}