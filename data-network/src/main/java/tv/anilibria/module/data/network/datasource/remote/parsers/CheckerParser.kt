package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONObject
import tv.anilibria.module.data.network.datasource.remote.IApiUtils
import tv.anilibria.module.data.network.datasource.remote.address.ApiAddressResponse
import tv.anilibria.module.data.network.datasource.remote.address.ApiProxy
import tv.anilibria.module.data.network.entity.app.updater.UpdateDataResponse
import ru.radiationx.shared.ktx.android.nullString
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
        val resData = UpdateDataResponse()
        val jsonUpdate = responseJson.getJSONObject("update")

        resData.code = jsonUpdate.optInt("version_code", Int.MAX_VALUE)
        resData.build = jsonUpdate.optInt("version_build", Int.MAX_VALUE)
        resData.name = jsonUpdate.optString("version_name")
        resData.date = jsonUpdate.optString("build_date")

        jsonUpdate.getJSONArray("links")?.let {
            for (i in 0 until it.length()) {
                it.optJSONObject(i)?.let { linkJson ->
                    resData.links.add(UpdateDataResponse.UpdateLink().apply {
                        name = linkJson.optString("name", "Unknown")
                        url = linkJson.optString("url", "")
                        type = linkJson.optString("type", "site")
                    })
                }
            }
        }

        jsonUpdate.getJSONArray("important")?.let {
            for (i in 0 until it.length()) {
                it.optString(i, null)?.let {
                    resData.important.add(it)
                }
            }
        }

        jsonUpdate.getJSONArray("added")?.let {
            for (i in 0 until it.length()) {
                it.optString(i, null)?.let {
                    resData.added.add(it)
                }
            }
        }

        jsonUpdate.getJSONArray("fixed")?.let {
            for (i in 0 until it.length()) {
                it.optString(i, null)?.let {
                    resData.fixed.add(it)
                }
            }
        }

        jsonUpdate.getJSONArray("changed")?.let {
            for (i in 0 until it.length()) {
                it.optString(i, null)?.let {
                    resData.changed.add(it)
                }
            }
        }

        return resData
    }
}