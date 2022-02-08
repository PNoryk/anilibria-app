package tv.anilibria.feature.networkconfig.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiProxyResponse(
    @Json(name = "tag") val tag: String,
    @Json(name = "name") val name: String?,
    @Json(name = "desc") val desc: String?,
    @Json(name = "ip") val ip: String,
    @Json(name = "port") val port: Int,
    @Json(name = "user") val user: String?,
    @Json(name = "password") val password: String?,
)