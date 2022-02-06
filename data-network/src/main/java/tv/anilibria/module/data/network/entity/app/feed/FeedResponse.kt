package tv.anilibria.module.data.network.entity.app.feed

import tv.anilibria.module.data.network.entity.app.release.ReleaseItem
import tv.anilibria.module.data.network.entity.app.youtube.YoutubeResponse

data class FeedResponse(
        var release: ReleaseItem? = null,
        var youtube: YoutubeResponse? = null
)