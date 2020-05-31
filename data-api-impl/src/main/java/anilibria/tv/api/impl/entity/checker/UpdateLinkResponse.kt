package anilibria.tv.api.impl.entity.checker


import com.google.gson.annotations.SerializedName

data class UpdateLinkResponse(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
    @SerializedName("type") val type: String
)