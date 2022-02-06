package tv.anilibria.module.data.network.datasource.remote.address

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiConfigResponse(
    @Json(name = "addresses") val addresses: List<ApiAddressResponse>
)