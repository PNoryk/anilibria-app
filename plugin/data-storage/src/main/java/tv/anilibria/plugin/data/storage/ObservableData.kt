package tv.anilibria.plugin.data.storage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.util.concurrent.atomic.AtomicBoolean

class ObservableData<T>(
    private val persistableData: DataHolder<T> = InMemoryDataHolder()
) {

    private val needUpdate = AtomicBoolean(true)

    private val triggerRelay = MutableSharedFlow<Unit>()

    private val inMemoryData = InMemoryDataHolder<T>()

    fun observe(): Flow<DataWrapper<T>> = triggerRelay
        .onStart { emit(Unit) }
        .map { getActualData() }

    suspend fun get(): DataWrapper<T> = getActualData()

    suspend fun put(data: DataWrapper<T>) {
        persistableData.save(data)
        needUpdate.set(true)
        triggerRelay.emit(Unit)
    }

    suspend fun update(callback: (DataWrapper<T>) -> DataWrapper<T>) {
        put(callback.invoke(get()))
    }

    private suspend fun getActualData(): DataWrapper<T> {
        return if (needUpdate.compareAndSet(true, false)) {
            persistableData.get()
                .also { inMemoryData.save(it) }
            inMemoryData.get()
        } else {
            inMemoryData.get()
        }
    }

}