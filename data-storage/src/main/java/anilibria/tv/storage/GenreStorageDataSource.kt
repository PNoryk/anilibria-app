package anilibria.tv.storage

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface GenreStorageDataSource {

    fun getList(): Single<List<String>>

    fun putList(items: List<String>): Completable
}