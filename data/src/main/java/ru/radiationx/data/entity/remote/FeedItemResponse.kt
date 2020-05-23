package ru.radiationx.data.entity.remote


import com.google.gson.annotations.SerializedName

data class FeedItemResponse(
    @SerializedName("release") val release: ReleaseResponse?,
    @SerializedName("youtube") val youtube: YouTubeResponse?
)