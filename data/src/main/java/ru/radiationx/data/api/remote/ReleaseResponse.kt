package ru.radiationx.data.api.remote


import com.google.gson.annotations.SerializedName

data class ReleaseResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("code") val code: String?,
    @SerializedName("names") val names: List<String>?,
    @SerializedName("series") val series: String?,
    @SerializedName("poster") val poster: String?,
    @SerializedName("favorite") val favorite: FavoriteInfoResponse?,
    @SerializedName("last") val last: String?,
    @SerializedName("moon") val moon: String?,
    @SerializedName("statusCode") val statusCode: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("genres") val genres: List<String>?,
    @SerializedName("voices") val voices: List<String>?,
    @SerializedName("year") val year: String?,
    @SerializedName("day") val day: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("blockedInfo") val blockedInfo: BlockInfoResponse?,
    @SerializedName("playlist") val playlist: List<EpisodeResponse>?,
    @SerializedName("torrents") val torrents: List<TorrentResponse>?
)