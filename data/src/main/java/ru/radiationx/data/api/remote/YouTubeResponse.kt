package ru.radiationx.data.api.remote


import com.google.gson.annotations.SerializedName

data class YouTubeResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("vid") val vid: String?,
    @SerializedName("views") val views: Int,
    @SerializedName("comments") val comments: Int,
    @SerializedName("timestamp") val timestamp: Int
)