package tv.anilibria.feature.networkconfig.data

import com.stealthcopter.networktools.ping.PingOptions
import com.stealthcopter.networktools.ping.PingResult
import com.stealthcopter.networktools.ping.PingTools
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withTimeout
import tv.anilibria.feature.networkconfig.data.domain.ApiAddress
import java.net.InetAddress
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class ConfigurationRepository @Inject constructor(
    private val configurationApi: ConfigRemoteDataSource
) {
    private val pingRelay = MutableStateFlow(mapOf<String, PingResult>())

    suspend fun checkAvailable(apiUrl: String): Boolean = configurationApi
        .checkAvailable(apiUrl)

    suspend fun checkApiAvailable(apiUrl: String): Boolean = configurationApi
        .checkApiAvailable(apiUrl)

    suspend fun getConfiguration(): List<ApiAddress> = configurationApi
        .getConfiguration()

    suspend fun getPingHost(host: String): PingResult {
        val result = withTimeout(15L.seconds) {
            PingTools.doNativePing(InetAddress.getByName(host), PingOptions())
        }
        val map = pingRelay.value.toMutableMap()
        map[host] = result
        pingRelay.value = map
        return result
    }
}