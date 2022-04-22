package tv.anilibria.plugin.data.storage

import java.util.concurrent.atomic.AtomicReference

class InMemoryDataHolder<T>(defaultValue: T? = null) : DataHolder<T> {

    private val atomicReference = AtomicReference<T>(defaultValue)

    override suspend fun get(): T {
        return atomicReference.get()
    }

    override suspend fun save(data: T) {
        atomicReference.lazySet(data)
    }
}