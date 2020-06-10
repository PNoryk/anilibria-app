package anilibria.tv.db

import anilibria.tv.domain.entity.common.keys.FeedKey
import anilibria.tv.domain.entity.relative.FeedRelative
import io.reactivex.Completable
import io.reactivex.Single

interface FeedDbDataSource {
    fun getList(): Single<List<FeedRelative>>
    fun getSome(keys: List<FeedKey>): Single<List<FeedRelative>>
    fun getOne(key: FeedKey): Single<FeedRelative>
    fun insert(items: List<FeedRelative>): Completable
    fun remove(keys: List<FeedKey>): Completable
    fun clear(): Completable
}