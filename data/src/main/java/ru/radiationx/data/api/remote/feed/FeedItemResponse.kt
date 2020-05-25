package ru.radiationx.data.api.remote.feed


import com.google.gson.annotations.SerializedName
import ru.radiationx.data.api.remote.youtube.YouTubeResponse
import ru.radiationx.data.api.remote.release.ReleaseResponse

data class FeedItemResponse(
    @SerializedName("release") val release: ReleaseResponse?,
    @SerializedName("youtube") val youtube: YouTubeResponse?
)