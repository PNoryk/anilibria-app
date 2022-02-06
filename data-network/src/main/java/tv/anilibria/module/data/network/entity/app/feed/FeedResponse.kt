package tv.anilibria.module.data.network.entity.app.feed

import tv.anilibria.module.data.network.entity.app.release.ReleaseItem
import tv.anilibria.module.data.network.entity.app.youtube.YoutubeResponse

data class FeedResponse(
    val release: ReleaseItem? = null,
    val youtube: YoutubeResponse? = null
)