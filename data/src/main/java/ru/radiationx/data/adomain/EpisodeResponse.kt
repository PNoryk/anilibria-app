package ru.radiationx.data.adomain


import com.google.gson.annotations.SerializedName

data class EpisodeResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("sd") val sd: String?,
    @SerializedName("hd") val hd: String?,
    @SerializedName("fullhd") val fullhd: String?,
    @SerializedName("srcSd") val srcSd: String?,
    @SerializedName("srcHd") val srcHd: String?
)