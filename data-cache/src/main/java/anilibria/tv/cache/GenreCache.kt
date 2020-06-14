package anilibria.tv.cache

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface GenreCache {

    fun observeList(): Observable<List<String>>

    fun getList(): Single<List<String>>

    fun putList(items: List<String>): Completable

    fun clear(): Completable
}