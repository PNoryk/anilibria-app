package tv.anilibria.module.data.local.holders

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface YearsLocalDataSource {
    fun observe(): Observable<List<String>>
    fun get(): Single<List<String>>
    fun put(data: List<String>): Completable
}