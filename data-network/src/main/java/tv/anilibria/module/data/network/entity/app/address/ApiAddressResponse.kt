package tv.anilibria.module.data.network.entity.app.address

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiAddressResponse(
    @Json(name = "tag") val tag: String,
    @Json(name = "name") val name: String?,
    @Json(name = "desc") val desc: String?,
    @Json(name = "widgetsSite") val widgetsSite: String,
    @Json(name = "site") val site: String,
    @Json(name = "baseImages") val baseImages: String,
    @Json(name = "base") val base: String,
    @Json(name = "api") val api: String,
    @Json(name = "ips") val ips: List<String>,
    @Json(name = "proxies") val proxies: List<ApiProxyResponse>
)