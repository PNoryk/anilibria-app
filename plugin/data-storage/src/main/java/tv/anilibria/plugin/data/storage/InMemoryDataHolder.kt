package tv.anilibria.plugin.data.storage

import java.util.concurrent.atomic.AtomicReference

class InMemoryDataHolder<T> : DataHolder<T> {

    private val atomicReference = AtomicReference<DataWrapper<T>>(DataWrapper(null))

    override suspend fun get(): DataWrapper<T> {
        return atomicReference.get()
    }

    override suspend fun save(data: DataWrapper<T>) {
        atomicReference.lazySet(data)
    }
}