package anilibria.tv.storage

import io.reactivex.Completable
import io.reactivex.Single

interface YearStorageDataSource {

    fun getList(): Single<List<String>>

    fun putList(items: List<String>): Completable

    fun clear(): Completable
}