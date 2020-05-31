package anilibria.tv.api.impl.entity.feed


import com.google.gson.annotations.SerializedName
import anilibria.tv.api.impl.entity.release.ReleaseResponse
import anilibria.tv.api.impl.entity.youtube.YouTubeResponse

data class FeedItemResponse(
    @SerializedName("release") val release: ReleaseResponse?,
    @SerializedName("youtube") val youtube: YouTubeResponse?
)