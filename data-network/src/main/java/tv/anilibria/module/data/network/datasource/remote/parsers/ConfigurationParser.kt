package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONObject
import ru.radiationx.shared.ktx.android.mapObjects
import ru.radiationx.shared.ktx.android.mapStrings
import ru.radiationx.shared.ktx.android.nullString
import tv.anilibria.module.data.network.datasource.remote.address.ApiAddressResponse
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfigResponse
import tv.anilibria.module.data.network.datasource.remote.address.ApiProxyResponse
import javax.inject.Inject

/**
 * Created by radiationx on 27.01.18.
 */
class ConfigurationParser @Inject constructor() {

    fun parse(responseJson: JSONObject): ApiConfigResponse {
        val result = responseJson.getJSONArray("addresses")?.mapObjects { addressJson ->
            parseAddress(addressJson)
        }.orEmpty()
        return ApiConfigResponse(result)
    }

    private fun parseAddress(addressJson: JSONObject): ApiAddressResponse {
        val ips = addressJson.getJSONArray("ips").mapStrings { it }

        val proxies = addressJson.getJSONArray("proxies").mapObjects { proxyJson ->
            parseProxy(proxyJson)
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

    private fun parseProxy(proxyJson: JSONObject): ApiProxyResponse = ApiProxyResponse(
        tag = proxyJson.getString("tag"),
        name = proxyJson.nullString("name"),
        desc = proxyJson.nullString("desc"),
        ip = proxyJson.getString("ip"),
        port = proxyJson.getInt("port"),
        user = proxyJson.nullString("user"),
        password = proxyJson.nullString("password")
    )
}