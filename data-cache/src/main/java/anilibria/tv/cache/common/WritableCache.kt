package anilibria.tv.cache.common

import anilibria.tv.domain.entity.common.MemoryKey
import io.reactivex.Completable

interface WritableCache<K : MemoryKey, T> {

    fun insert(items: List<T>): Completable

    fun remove(keys: List<K>): Completable

    fun clear(): Completable
}