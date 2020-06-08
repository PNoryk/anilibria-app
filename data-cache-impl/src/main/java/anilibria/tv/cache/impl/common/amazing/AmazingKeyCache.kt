package anilibria.tv.cache.impl.common.amazing

import java.util.*

class AmazingKeyCache<K : MemoryKey> {

    private val keysCache by lazy { Collections.synchronizedMap(mutableMapOf<K, MutableSet<K>>()) }

    fun get(key: K): Set<K>? {
        return keysCache[key]
    }

    fun get(keys: List<K>): Set<K> {
        val result = mutableSetOf<K>()
        keys.forEach { key ->
            val cachedKey = keysCache[key]
            if (cachedKey != null) {
                result.addAll(cachedKey)
            }
        }
        return result
    }

    fun insert(keys: List<K>) {
        keys.forEach {
            insert(it, setOf(it))
        }
    }

    fun insert(key: K, keys: Set<K>) {
        if (!keysCache.containsKey(key)) {
            keysCache[key] = Collections.synchronizedSet(mutableSetOf())
        }
        keysCache.getValue(key).addAll(keys)
    }

    fun remove(keys: List<K>) {
        keysCache.values.forEach {
            it.removeAll(keys)
        }
        keysCache.minusAssign(keys)
    }

    fun clear() {
        keysCache.values.forEach {
            it.clear()
        }
        keysCache.clear()
    }
}