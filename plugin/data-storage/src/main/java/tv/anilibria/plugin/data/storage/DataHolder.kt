package tv.anilibria.plugin.data.storage

import io.reactivex.Completable
import io.reactivex.Single

interface DataHolder<T> {
    fun get(): Single<DataWrapper<T>>
    fun save(data: DataWrapper<T>): Completable
}