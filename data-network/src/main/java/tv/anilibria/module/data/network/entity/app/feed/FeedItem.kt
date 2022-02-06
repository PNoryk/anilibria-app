package tv.anilibria.module.data.network.entity.app.feed

import tv.anilibria.module.data.network.entity.app.release.ReleaseItem
import tv.anilibria.module.data.network.entity.app.youtube.YoutubeItem

data class FeedItem(
        var release: ReleaseItem? = null,
        var youtube: YoutubeItem? = null
)