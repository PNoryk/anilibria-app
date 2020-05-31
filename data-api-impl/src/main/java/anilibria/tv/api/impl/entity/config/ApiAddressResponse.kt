package anilibria.tv.api.impl.entity.config


import com.google.gson.annotations.SerializedName

data class ApiAddressResponse(
    @SerializedName("tag") val tag: String,
    @SerializedName("name") val name: String?,
    @SerializedName("desc") val desc: String?,
    @SerializedName("widgetsSite") val widgetsSite: String,
    @SerializedName("site") val site: String,
    @SerializedName("baseImages") val baseImages: String,
    @SerializedName("base") val base: String,
    @SerializedName("api") val api: String,
    @SerializedName("ips") val ips: List<String>,
    @SerializedName("proxies") val proxies: List<ApiProxyResponse>
)