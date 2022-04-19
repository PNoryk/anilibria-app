package tv.anilibria.feature.networkconfig.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import toothpick.InjectConstructor
import tv.anilibria.feature.networkconfig.data.domain.ApiAddress
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse
import kotlin.time.Duration.Companion.seconds

@InjectConstructor
class ConfigRemoteDataSource(
    private val configApi: ConfigApiWrapper,
    private val reserveSources: ApiConfigReserveSources
) {

    suspend fun checkAvailable(apiUrl: String): Boolean = withTimeout(15L.seconds) {
        check(configApi.proxy(), apiUrl)
    }

    suspend fun checkApiAvailable(apiUrl: String): Boolean = withTimeout(15L.seconds) {
        runCatching { check(configApi.proxy(), apiUrl) }.isSuccess
    }

    suspend fun getConfiguration(): List<ApiAddress> {
        val fromApi = withContext(Dispatchers.IO) {
            runCatching { getConfigFromApi() }
                .getOrNull()
                .orEmpty()
        }
        val fromReserve = withContext(Dispatchers.IO) {
            runCatching { getConfigFromReserve() }
                .getOrNull()
                .orEmpty()
        }
        return listOf(fromApi, fromReserve)
            .firstOrNull { it.isNotEmpty() }
            ?: throw IllegalStateException("Empty config adresses")
    }

    private suspend fun check(client: ConfigApi, apiUrl: String): Boolean {
        client.checkAvailable(apiUrl, formBodyOf("query" to "empty"))
        return true
    }

    private suspend fun getConfigFromApi(): List<ApiAddress> = withTimeout(10L.seconds) {
        val body = formBodyOf("query" to "config")
        configApi.proxy()
            .getConfig(body)
            .handleApiResponse()
            .toDomain().addresses
    }

    private suspend fun getConfigFromReserve(): List<ApiAddress> {
        val singleSources = reserveSources.sources.map { source ->
            runCatching { getReserve(source) }
        }
        return singleSources
            .map { it.getOrNull() }
            .firstOrNull { it != null }
            .orEmpty()
    }

    private suspend fun getReserve(url: String): List<ApiAddress> {
        return configApi.direct().getReserveConfig(url).toDomain().addresses
    }
}