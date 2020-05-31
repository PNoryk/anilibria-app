package anilibria.tv.domain.entity.feed

import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.youtube.Youtube


data class Feed(
    val release: Release?,
    val youtube: Youtube?
)