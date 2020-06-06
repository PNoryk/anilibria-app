package anilibria.tv.cache.impl.common.amazing

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

open class AmazingMemoryDataSource<T : Any> {

    private val memoryMap by lazy { Collections.synchronizedMap(mutableMapOf<MemoryKey, T>()) }

    private val dataRelay by lazy { BehaviorRelay.createDefault(getMemoryList()) }

    /* Observe */
    fun observeOne(key: MemoryKey): Observable<T> = dataRelay
        .filter { memoryMap.contains(key) }
        .map { memoryMap.getValue(key) }

    fun observeList(): Observable<List<T>> = dataRelay.hide()

    fun observeSome(keys: List<MemoryKey>): Observable<List<T>> = dataRelay
        .map { getMemoryList(keys) }


    /* Read */
    fun getOne(key: MemoryKey): Maybe<T> = Maybe.fromCallable {
        memoryMap[key]
    }

    fun getList(): Single<List<T>> = Single.fromCallable {
        getMemoryList()
    }

    fun getSome(keys: List<MemoryKey>): Single<List<T>> = Single.fromCallable {
        getMemoryList(keys)
    }


    /* Insert */
    fun insert(items: List<Pair<MemoryKey, T>>): Completable = Completable.fromAction {
        memoryMap.plusAssign(items)
        updateRelay()
    }


    /* Delete */
    fun removeList(keys: List<MemoryKey>): Completable = Completable.fromAction {
        memoryMap.minusAssign(keys)
        updateRelay()
    }

    fun clear(): Completable = Completable.fromAction {
        memoryMap.clear()
        updateRelay()
    }

    /* Common */
    private fun getMemoryList(): List<T> = memoryMap.values.toList()

    private fun getMemoryList(keys: List<MemoryKey>): List<T> = keys.mapNotNull { memoryMap[it] }

    private fun updateRelay() {
        dataRelay.accept(getMemoryList())
    }
}