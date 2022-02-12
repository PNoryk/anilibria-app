package tv.anilibria.feature.networkconfig.data

import kotlinx.coroutines.flow.Flow
import tv.anilibria.feature.networkconfig.data.domain.ApiAddress
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.DataWrapper
import tv.anilibria.plugin.data.storage.ObservableData

class ConfigLocalDataStorage(
    private val storage: DataStorage,
) {

    private val observableData = ObservableData<List<ApiAddress>>()

    private val observableActive = ObservableData<String>()

    fun observe(): Flow<DataWrapper<List<ApiAddress>>> = observableData.observe()

    suspend fun set(data: List<ApiAddress>) = observableData.put(DataWrapper(data))

    suspend fun get(): DataWrapper<List<ApiAddress>> = observableData.get()

    fun observeActive(): Flow<DataWrapper<String>> = observableActive.observe()

    suspend fun getActive(): DataWrapper<String> = observableActive.get()

    suspend fun setActive(tag: String) = observableActive.put(DataWrapper(tag))
}