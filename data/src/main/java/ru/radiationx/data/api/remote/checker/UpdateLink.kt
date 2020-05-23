package ru.radiationx.data.api.remote.checker


import com.google.gson.annotations.SerializedName

data class UpdateLink(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
    @SerializedName("type") val type: String
)