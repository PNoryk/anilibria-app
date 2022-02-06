package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONObject
import ru.radiationx.shared.ktx.android.nullString
import tv.anilibria.module.data.network.datasource.remote.address.ApiAddressResponse
import tv.anilibria.module.data.network.datasource.remote.address.ApiProxy
import javax.inject.Inject

/**
 * Created by radiationx on 27.01.18.
 */
class ConfigurationParser @Inject constructor() {

    fun parse(responseJson: JSONObject): List<ApiAddressResponse> {
        val result = mutableListOf<ApiAddressResponse>()
        responseJson.getJSONArray("addresses")?.let {
            for (i in 0 until it.length()) {
                it.optJSONObject(i)?.let { addressJson ->
                    result.add(parseAddress(addressJson))
                }
            }
        }
        return result
    }

    private fun parseAddress(addressJson: JSONObject): ApiAddressResponse {
        val ips = mutableListOf<String>()
        addressJson.getJSONArray("ips")?.let {
            for (i in 0 until it.length()) {
                it.getString(i)?.also {
                    ips.add(it)
                }
            }
        }

        val proxies = mutableListOf<ApiProxy>()
        addressJson.getJSONArray("proxies")?.let {
            for (i in 0 until it.length()) {
                it.optJSONObject(i)?.also { proxyJson ->
                    proxies.add(parseProxy(proxyJson))
                }
            }
        }
        return ApiAddressResponse(
            tag = addressJson.getString("tag"),
            name = addressJson.nullString("name"),
            desc = addressJson.nullString("desc"),
            widgetsSite = addressJson.getString("widgetsSite"),
            site = addressJson.getString("site"),
            baseImages = addressJson.getString("baseImages"),
            base = addressJson.getString("base"),
            api = addressJson.getString("api"),
            ips = ips,
            proxies = proxies
        )
    }

    private fun parseProxy(proxyJson: JSONObject): ApiProxy = ApiProxy(
        proxyJson.getString("tag"),
        proxyJson.nullString("name"),
        proxyJson.nullString("desc"),
        proxyJson.getString("ip"),
        proxyJson.getInt("port"),
        proxyJson.nullString("user"),
        proxyJson.nullString("password")
    )
}