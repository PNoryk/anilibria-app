package ru.radiationx.data.adomain.feed

import ru.radiationx.data.adomain.release.Release
import ru.radiationx.data.adomain.youtube.YouTube


data class FeedItem(
    val release: Release?,
    val youtube: YouTube?
)