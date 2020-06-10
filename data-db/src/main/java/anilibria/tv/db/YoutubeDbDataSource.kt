package anilibria.tv.db

import anilibria.tv.domain.entity.common.keys.YoutubeKey
import anilibria.tv.domain.entity.youtube.Youtube
import io.reactivex.Completable
import io.reactivex.Single

interface YoutubeDbDataSource {
    fun getList(): Single<List<Youtube>>
    fun getSome(keys: List<YoutubeKey>): Single<List<Youtube>>
    fun getOne(key: YoutubeKey): Single<Youtube>
    fun insert(items: List<Youtube>): Completable
    fun remove(keys: List<YoutubeKey>): Completable
    fun clear(): Completable
}