package tv.anilibria.feature.networkconfig.data

import com.stealthcopter.networktools.ping.PingOptions
import com.stealthcopter.networktools.ping.PingResult
import com.stealthcopter.networktools.ping.PingTools
import kotlinx.coroutines.withTimeout
import toothpick.InjectConstructor
import tv.anilibria.feature.networkconfig.data.domain.ApiAddress
import java.net.InetAddress
import kotlin.time.Duration.Companion.seconds

@InjectConstructor
class ConfigurationRepository(
    private val configurationApi: ConfigRemoteDataSource,
    private val pingCache: ConfigPingCache
) {

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
        pingCache.proxies.update {
            it.toMutableMap().apply {
                if (!result.hasError()) {
                    put(host, result.getTimeTaken().toLong())
                }
            }
        }
        return result
    }
}