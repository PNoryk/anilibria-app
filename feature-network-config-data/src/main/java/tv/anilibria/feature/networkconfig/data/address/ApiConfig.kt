package ru.radiationx.data.datasource.remote.address

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import tv.anilibria.feature.networkconfig.data.ConfigLocalDataStorage
import tv.anilibria.feature.networkconfig.data.address.Api
import tv.anilibria.feature.networkconfig.data.domain.ApiAddress
import tv.anilibria.feature.networkconfig.data.domain.ApiProxy
import javax.inject.Inject

class ApiConfig @Inject constructor(
    private val configChanger: ApiConfigChanger,
    private val apiConfigStorage: ConfigLocalDataStorage
) {

    private val needConfigRelay = MutableSharedFlow<Boolean>()
    var needConfig = true

    fun observeNeedConfig(): Flow<Boolean> = needConfigRelay.asSharedFlow()

    suspend fun updateNeedConfig(state: Boolean) {
        needConfig = state
        needConfigRelay.emit(needConfig)
    }

    suspend fun updateActiveAddress(address: ApiAddress) {
        apiConfigStorage.setActive(address.tag)
        configChanger.onChange()
    }

    suspend fun setAddresses(items: List<ApiAddress>) {
        apiConfigStorage.set(items)
    }

    suspend fun getAddresses(): List<ApiAddress> = apiConfigStorage.get().orEmpty()

    suspend fun getActive(): ApiAddress {
        val activeTag = apiConfigStorage.getActive()
        val activeByTag = apiConfigStorage.get()?.find { it.tag == activeTag }
        return activeByTag ?: Api.DEFAULT_ADDRESS
    }

}