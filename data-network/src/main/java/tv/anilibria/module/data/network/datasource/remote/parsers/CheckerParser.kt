package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONObject
import ru.radiationx.shared.ktx.android.mapObjects
import ru.radiationx.shared.ktx.android.mapStrings
import ru.radiationx.shared.ktx.android.nullString
import tv.anilibria.module.data.network.datasource.remote.IApiUtils
import tv.anilibria.module.data.network.datasource.remote.address.ApiAddressResponse
import tv.anilibria.module.data.network.datasource.remote.address.ApiProxy
import tv.anilibria.module.data.network.entity.app.updater.UpdateDataResponse
import tv.anilibria.module.data.network.entity.app.updater.UpdateLinkResponse
import javax.inject.Inject

/**
 * Created by radiationx on 27.01.18.
 */
class CheckerParser @Inject constructor(
    private val apiUtils: IApiUtils
) {

    fun parseAddresses(responseJson: JSONObject): List<ApiAddressResponse> {
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
            addressJson.getString("tag"),
            addressJson.nullString("name"),
            addressJson.nullString("desc"),
            addressJson.getString("widgetsSite"),
            addressJson.getString("site"),
            addressJson.getString("baseImages"),
            addressJson.getString("base"),
            addressJson.getString("api"),
            ips,
            proxies
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

    fun parse(responseJson: JSONObject): UpdateDataResponse {
        val jsonUpdate = responseJson.getJSONObject("update")

        val links = jsonUpdate.getJSONArray("links").mapObjects { linkJson ->
            UpdateLinkResponse(
                linkJson.getString("name"),
                linkJson.getString("url"),
                linkJson.getString("type")
            )
        }
        val important = jsonUpdate.getJSONArray("important").mapStrings { it }
        val added = jsonUpdate.getJSONArray("added").mapStrings { it }
        val fixed = jsonUpdate.getJSONArray("fixed").mapStrings { it }
        val changed = jsonUpdate.getJSONArray("changed").mapStrings { it }
        return UpdateDataResponse(
            jsonUpdate.getInt("version_code"),
            jsonUpdate.getInt("version_build"),
            jsonUpdate.getString("version_name"),
            jsonUpdate.getString("build_date"),
            links,
            important,
            added,
            fixed,
            changed
        )
    }
}