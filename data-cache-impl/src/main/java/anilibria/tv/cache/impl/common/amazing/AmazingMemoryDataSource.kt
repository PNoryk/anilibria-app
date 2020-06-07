package anilibria.tv.cache.impl.common.amazing

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import kotlin.NoSuchElementException

open class AmazingMemoryDataSource<K : MemoryKey, V : Any> {

    private val memoryMap by lazy { Collections.synchronizedMap(mutableMapOf<K, V>()) }

    private val columnIndices by lazy { Collections.synchronizedList(mutableListOf<MutableMap<Any?, MutableSet<K>>>()) }

    private val dataRelay by lazy { BehaviorRelay.createDefault(getMemoryList()) }

    /* Observe */
    fun observeOne(key: K): Observable<V> = observeSome(listOf(key))
        .filter { it.isNotEmpty() }
        .map { it.first() }

    fun observeList(): Observable<List<V>> = dataRelay.hide()

    fun observeSome(keys: List<K>): Observable<List<V>> = dataRelay
        .map {
            keys.firstOrNull()?.also { createColumnIndices(it) }
            getMemoryList(keys)
        }


    /* Read */
    fun getOne(key: K): Single<V> = Single.fromCallable {
        createColumnIndices(key)
        getMemoryOne(key) ?: throw NoSuchElementException()
    }

    fun getList(): Single<List<V>> = Single.fromCallable {
        getMemoryList()
    }

    fun getSome(keys: List<K>): Single<List<V>> = Single.fromCallable {
        keys.firstOrNull()?.also { createColumnIndices(it) }
        getMemoryList(keys)
    }


    /* Insert */
    fun insert(items: List<Pair<K, V>>): Completable = Completable.fromAction {
        items.firstOrNull()?.also { createColumnIndices(it.first) }

        items.forEach { pair ->
            val key = pair.first
            key.columns.forEachIndexed { columnIndex, columnValue ->
                putIndex(columnIndex, columnValue, key)
            }
        }

        memoryMap.plusAssign(items)
        updateRelay()
    }


    /* Delete */
    fun removeList(keys: List<K>): Completable = Completable.fromAction {
        keys.firstOrNull()?.also { createColumnIndices(it) }

        keys.forEach { key ->
            key.columns.forEachIndexed { columnIndex, columnValue ->
                removeIndex(columnIndex, columnValue, key)
            }
        }

        memoryMap.minusAssign(keys)
        updateRelay()
    }

    fun clear(): Completable = Completable.fromAction {
        memoryMap.clear()
        columnIndices.forEach { columnMap ->
            columnMap.values.forEach { columnKeys ->
                columnKeys.clear()
            }
            columnMap.clear()
        }
        columnIndices.clear()
        updateRelay()
    }

    /* Common */

    private fun createColumnIndices(key: K) {
        if (columnIndices.isNotEmpty()) return
        repeat(key.columns.size) {
            columnIndices.add(Collections.synchronizedMap(mutableMapOf<Any?, MutableSet<K>>()))
        }
    }

    private fun putIndex(index: Int, columnValue: Any?, key: K) {
        val columnMap = columnIndices[index]
        if (!columnMap.containsKey(columnValue)) {
            columnMap[columnValue] = mutableSetOf()
        }
        columnMap.getValue(columnValue).add(key)
    }

    private fun getIndexColumn(index: Int, columnValue: Any?): Set<K>? {
        return columnIndices[index][columnValue]
    }

    private fun removeIndex(index: Int, columnValue: Any?, key: K) {
        val columnMap = columnIndices[index]
        columnMap[columnValue]?.remove(key)
    }

    private fun getMemoryOne(key: K): V? = getMemoryList(listOf(key)).firstOrNull()

    private fun getMemoryList(): List<V> = memoryMap.values.toList()

    private fun getMemoryList(keys: List<K>): List<V> = findSameKeys(keys).mapNotNull { memoryMap[it] }

    private fun findSameKeys(keys: List<K>): Set<K> {
        val result = mutableSetOf<K>()
        keys.forEach { key ->
            println("find by key ${key}")
            key.columns.forEachIndexed { columnIndex, columnValue ->
                getIndexColumn(columnIndex, columnValue)?.also { columnKeys ->
                    columnKeys.forEach { columnKey ->
                        if (key.hasAnyEqualKeys(columnKey)) {
                            result.add(columnKey)
                        }
                    }
                }
            }
        }
        return result
    }

    private fun updateRelay() {
        dataRelay.accept(getMemoryList())
    }
}