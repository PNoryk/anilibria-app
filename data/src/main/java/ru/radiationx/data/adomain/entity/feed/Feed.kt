package ru.radiationx.data.adomain.entity.feed

import ru.radiationx.data.adomain.entity.release.Release
import ru.radiationx.data.adomain.entity.youtube.Youtube


data class Feed(
    val release: Release?,
    val youtube: Youtube?
)