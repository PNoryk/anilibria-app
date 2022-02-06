package tv.anilibria.module.domain.entity.feed

import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.module.domain.entity.youtube.Youtube

data class Feed(
    val release: Release? = null,
    val youtube: Youtube? = null
)