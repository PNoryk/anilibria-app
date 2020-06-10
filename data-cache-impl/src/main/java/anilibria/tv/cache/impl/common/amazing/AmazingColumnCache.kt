package anilibria.tv.cache.impl.common.amazing

import anilibria.tv.domain.entity.common.MemoryKey
import java.util.*

class AmazingColumnCache<K : MemoryKey> {

    private val columnIndices by lazy { Collections.synchronizedList(mutableListOf<MutableMap<Any?, MutableSet<K>>>()) }

    fun get(keys: List<K>): Set<K> {
        keys.firstOrNull()?.also { createColumnIndices(it) }
        val result = mutableSetOf<K>()
        keys.forEach { key ->
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

    fun insert(keys: List<K>) {
        keys.firstOrNull()?.also { createColumnIndices(it) }

        keys.forEach { key ->
            key.columns.forEachIndexed { columnIndex, columnValue ->
                putIndex(columnIndex, columnValue, key)
            }
        }
    }

    fun remove(keys: List<K>) {
        keys.firstOrNull()?.also { createColumnIndices(it) }

        keys.forEach { key ->
            key.columns.forEachIndexed { columnIndex, columnValue ->
                removeIndex(columnIndex, columnValue, key)
            }
        }
    }

    fun clear() {
        columnIndices.forEach { columnMap ->
            columnMap.values.forEach { columnKeys ->
                columnKeys.clear()
            }
            columnMap.clear()
        }
        columnIndices.clear()
    }

    private fun createColumnIndices(key: K) {
        if (columnIndices.isNotEmpty()) return
        repeat(key.columns.size) {
            columnIndices.add(Collections.synchronizedMap(mutableMapOf<Any?, MutableSet<K>>()))
        }
    }

    private fun putIndex(index: Int, columnValue: Any?, key: K) {
        val columnMap = columnIndices[index]
        if (!columnMap.containsKey(columnValue)) {
            columnMap[columnValue] = Collections.synchronizedSet(mutableSetOf())
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

}