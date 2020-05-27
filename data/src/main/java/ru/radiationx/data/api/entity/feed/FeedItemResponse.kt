package ru.radiationx.data.api.entity.feed


import com.google.gson.annotations.SerializedName
import ru.radiationx.data.api.entity.youtube.YouTubeResponse
import ru.radiationx.data.api.entity.release.ReleaseResponse

data class FeedItemResponse(
    @SerializedName("release") val release: ReleaseResponse?,
    @SerializedName("youtube") val youtube: YouTubeResponse?
)