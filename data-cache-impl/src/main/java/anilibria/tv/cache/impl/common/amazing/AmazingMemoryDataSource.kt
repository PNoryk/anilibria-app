package anilibria.tv.cache.impl.common.amazing

import anilibria.tv.domain.entity.common.MemoryKey
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import kotlin.NoSuchElementException

open class AmazingMemoryDataSource<K : MemoryKey, V : Any> {

    private val memoryMap by lazy { Collections.synchronizedMap(mutableMapOf<K, V>()) }

    private val columnCache by lazy { AmazingColumnCache<K>() }

    private val keyCache by lazy { AmazingKeyCache<K>() }

    private val dataRelay by lazy { BehaviorRelay.createDefault(getMemoryList()) }

    /* Observe */
    fun observeOne(key: K): Observable<V> = observeSome(listOf(key))
        .filter { it.isNotEmpty() }
        .map { it.first() }

    fun observeList(): Observable<List<V>> = dataRelay.hide()

    fun observeSome(keys: List<K>): Observable<List<V>> = dataRelay
        .map {
            getMemoryList(keys)
        }


    /* Read */
    fun getOne(key: K): Single<V> = Single.fromCallable {
        getMemoryOne(key) ?: throw NoSuchElementException()
    }

    fun getList(): Single<List<V>> = Single.fromCallable {
        getMemoryList()
    }

    fun getSome(keys: List<K>): Single<List<V>> = Single.fromCallable {
        getMemoryList(keys)
    }


    /* Insert */
    fun insert(items: List<Pair<K, V>>): Completable = Completable.fromAction {
        val keys = items.map { it.first }
        keyCache.insert(keys)
        columnCache.insert(keys)
        memoryMap.plusAssign(items)
        updateRelay()
    }


    /* Delete */
    fun remove(keys: List<K>): Completable = Completable.fromAction {
        keyCache.remove(keys)
        columnCache.remove(keys)
        memoryMap.minusAssign(keys)
        updateRelay()
    }

    fun clear(): Completable = Completable.fromAction {
        keyCache.clear()
        memoryMap.clear()
        columnCache.clear()
        updateRelay()
    }

    /* Common */

    private fun getMemoryOne(key: K): V? = getMemoryList(listOf(key)).firstOrNull()

    private fun getMemoryList(): List<V> = memoryMap.values.toList()

    private fun getMemoryList(keys: List<K>): List<V> {
        keys.forEach { key ->
            val cachedKey = keyCache.get(key)
            if (cachedKey == null) {
                keyCache.insert(key, columnCache.get(listOf(key)))
            }
        }
        return keyCache.get(keys).mapNotNull { memoryMap[it] }
    }

    private fun updateRelay() {
        dataRelay.accept(getMemoryList())
    }
}