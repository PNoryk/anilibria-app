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

    private val triggerFlow = MutableSharedFlow<Unit>()

    private val inMemoryData = InMemoryDataHolder<T>()

    fun observe(): Flow<T> = triggerFlow
        .onStart { emit(Unit) }
        .map { getActualData() }

    suspend fun get(): T = getActualData()

    suspend fun put(data: T) {
        persistableData.save(data)
        needUpdate.set(true)
        triggerFlow.emit(Unit)
    }

    suspend fun update(callback: (T) -> T) {
        put(callback.invoke(get()))
    }

    fun triggerUpdate() {
        needUpdate.set(true)
        triggerFlow.tryEmit(Unit)
    }

    private suspend fun getActualData(): T {
        if (needUpdate.compareAndSet(true, false)) {
            persistableData.get().also {
                inMemoryData.save(it)
            }
        }
        return inMemoryData.get()
    }

}