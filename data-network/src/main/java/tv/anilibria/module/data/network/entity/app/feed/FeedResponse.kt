package tv.anilibria.module.data.network.entity.app.feed

import tv.anilibria.module.data.network.entity.app.release.ReleaseResponse
import tv.anilibria.module.data.network.entity.app.youtube.YoutubeResponse

data class FeedResponse(
    val release: ReleaseResponse? = null,
    val youtube: YoutubeResponse? = null
)