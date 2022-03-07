package tv.anilibria.feature.domain.entity.feed

import tv.anilibria.feature.domain.entity.release.Release
import tv.anilibria.feature.domain.entity.youtube.Youtube

data class Feed(
    val release: Release?,
    val youtube: Youtube?
)