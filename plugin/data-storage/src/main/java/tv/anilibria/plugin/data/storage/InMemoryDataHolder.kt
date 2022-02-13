package tv.anilibria.plugin.data.storage

import java.util.concurrent.atomic.AtomicReference

class InMemoryDataHolder<T> : DataHolder<T> {

    private val atomicReference = AtomicReference<T>(null)

    override suspend fun get(): T {
        return atomicReference.get()
    }

    override suspend fun save(data: T) {
        atomicReference.lazySet(data)
    }
}