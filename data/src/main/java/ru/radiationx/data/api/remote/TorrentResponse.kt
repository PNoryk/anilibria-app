package ru.radiationx.data.api.remote


import com.google.gson.annotations.SerializedName

data class TorrentResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("hash") val hash: String?,
    @SerializedName("leechers") val leechers: Int,
    @SerializedName("seeders") val seeders: Int,
    @SerializedName("completed") val completed: Int,
    @SerializedName("quality") val quality: String?,
    @SerializedName("series") val series: String?,
    @SerializedName("size") val size: Long,
    @SerializedName("ctime") val time: Long,
    @SerializedName("url") val url: String?
)