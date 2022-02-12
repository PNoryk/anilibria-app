package tv.anilibria.plugin.data.storage

import io.reactivex.Completable
import io.reactivex.Single

interface DataStorage {
    fun getString(key: String): Single<DataWrapper<String>>
    fun setString(key: String, value: DataWrapper<String>): Completable
    fun remove(key: String): Completable
    fun clear(): Completable
}