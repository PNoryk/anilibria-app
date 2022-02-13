package tv.anilibria.feature.networkconfig.data

import kotlinx.coroutines.flow.Flow
import tv.anilibria.feature.networkconfig.data.domain.ApiAddress
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.ObservableData

//todo добавить сохранение
class ConfigLocalDataStorage(
    private val storage: DataStorage,
) {

    private val observableData = ObservableData<List<ApiAddress>>()

    private val observableActive = ObservableData<String>()

    fun observe(): Flow<List<ApiAddress>?> = observableData.observe()

    suspend fun set(data: List<ApiAddress>) = observableData.put(data)

    suspend fun get(): List<ApiAddress> = observableData.get()

    fun observeActive(): Flow<String?> = observableActive.observe()

    suspend fun getActive(): String = observableActive.get()

    suspend fun setActive(tag: String) = observableActive.put(tag)
}