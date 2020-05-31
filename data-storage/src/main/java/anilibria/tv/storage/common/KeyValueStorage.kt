package anilibria.tv.storage.common

import io.reactivex.Completable
import io.reactivex.Maybe

interface KeyValueStorage {

    fun getValue(key: String): Maybe<String>

    fun putValue(key: String, value: String?): Completable

    fun delete(key: String): Completable
}