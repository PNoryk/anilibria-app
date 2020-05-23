package ru.radiationx.data.api.remote


import com.google.gson.annotations.SerializedName
import ru.radiationx.data.api.remote.ReleaseResponse
import ru.radiationx.data.api.remote.YouTubeResponse

data class FeedItemResponse(
    @SerializedName("release") val release: ReleaseResponse?,
    @SerializedName("youtube") val youtube: YouTubeResponse?
)