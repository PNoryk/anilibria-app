package tv.anilibria.feature.content.types.feed

import tv.anilibria.feature.content.types.release.Release
import tv.anilibria.feature.content.types.youtube.Youtube

data class Feed(
    val release: Release?,
    val youtube: Youtube?
)