package tv.anilibria.feature.networkconfig.data.address

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import tv.anilibria.feature.networkconfig.data.ConfigLocalDataStorage
import tv.anilibria.feature.networkconfig.data.domain.ApiAddress
import javax.inject.Inject

class ApiConfigController @Inject constructor(
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
        val activeByTag = apiConfigStorage.get().find { it.tag == activeTag }
        return activeByTag ?: Api.DEFAULT_ADDRESS
    }

    suspend fun getTag() = getActive().tag

}