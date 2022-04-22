package tv.anilibria.plugin.data.storage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.runBlocking

class ObservableData<T>(
    private val persistableData: DataHolder<T>
) {

    private var needUpdate = true

    private val triggerFlow = MutableSharedFlow<Unit>()

    private val inMemoryData = InMemoryDataHolder<T>()

    fun observe(): Flow<T> = triggerFlow
        .onStart { emit(Unit) }
        .map { getActualData() }

    suspend fun get(): T = getActualData()

    suspend fun put(data: T) {
        persistableData.save(data)
        needUpdate = true
        triggerFlow.emit(Unit)
    }

    suspend fun update(callback: (T) -> T) {
        put(callback.invoke(get()))
    }

    fun triggerUpdate() {
        runBlocking {
            needUpdate = true
            triggerFlow.emit(Unit)
        }
    }

    private suspend fun getActualData(): T {
        if (needUpdate) {
            val savedData = persistableData.get()
            inMemoryData.save(savedData)
        }
        needUpdate = false
        return inMemoryData.get()
    }

    @Deprecated("use suspend get")
    fun blockingGet(): T = runBlocking { get() }

    @Deprecated("use suspend get")
    fun blockingSet(data: T) = runBlocking { put(data) }

}