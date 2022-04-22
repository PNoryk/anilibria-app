package tv.anilibria.feature.networkconfig.data.address

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import toothpick.InjectConstructor
import tv.anilibria.feature.networkconfig.data.ConfigLocalDataStorage
import tv.anilibria.feature.networkconfig.data.domain.ApiAddress

@InjectConstructor
class ApiConfigController(
    private val configChanger: ApiConfigChanger,
    private val apiConfigStorage: ConfigLocalDataStorage
) {

    private val needConfigRelay = MutableStateFlow(true)

    val needConfig: Boolean
        get() = needConfigRelay.value

    fun observeNeedConfig(): Flow<Boolean> = needConfigRelay.asStateFlow().onEach {
        Log.d("kekeke", "contr observeNeedConfig $it")
    }

    fun updateNeedConfig(state: Boolean) {
        Log.d("kekeke", "updateNeedConfig $state")
        needConfigRelay.value = state
        Log.d("kekeke", "success updateNeedConfig ${needConfigRelay.value}")
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