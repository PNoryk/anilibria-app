package anilibria.tv.db

import anilibria.tv.domain.entity.relative.FeedRelative
import io.reactivex.Completable
import io.reactivex.Single

interface FeedDbDataSource {
    fun getListAll(): Single<List<FeedRelative>>
    fun getList(ids: List<Pair<Int?, Int?>>): Single<List<FeedRelative>>
    fun getOne(releaseId: Int?, youtubeId: Int?): Single<FeedRelative>
    fun insert(items: List<FeedRelative>): Completable
    fun removeList(ids: List<Pair<Int?, Int?>>): Completable
    fun deleteAll(): Completable
}