package tv.anilibria.feature.networkconfig.data.okhttp

import kotlinx.coroutines.runBlocking
import okhttp3.*
import tv.anilibria.feature.networkconfig.data.ConfigPingCache
import tv.anilibria.feature.networkconfig.data.address.ApiConfig
import java.net.InetSocketAddress
import java.net.Proxy

class OkHttpProxyAppender(
    private val apiConfig: ApiConfig,
    private val pingCache: ConfigPingCache
) {

    fun appendTo(builder: OkHttpClient.Builder) {
        val bestProxy = runBlocking {
            val allProxies = apiConfig.getActive().proxies
            val cachedPings = pingCache.proxies.get().orEmpty()

            val randomProxy = allProxies.randomOrNull()
            val fastestProxy = allProxies
                .map { Pair(it, cachedPings[it.tag]) }
                .minByOrNull { it.second ?: 0 }
                ?.first
            fastestProxy ?: randomProxy
        } ?: return

        builder.proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress(bestProxy.ip, bestProxy.port)))
        val user = bestProxy.user
        val password = bestProxy.password
        if (user != null && password != null) {
            builder.proxyAuthenticator(ProxyAuthenticator(user, password))
        }
    }
}

private class ProxyAuthenticator(
    private val user: String,
    private val password: String
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val credential = Credentials.basic(user, password)
        return response.request().newBuilder()
            .header("Proxy-Authorization", credential)
            .build()
    }
}