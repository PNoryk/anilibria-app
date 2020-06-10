package anilibria.tv.domain.entity.common.keys

import anilibria.tv.domain.entity.common.MemoryKey

data class FeedKey(val releaseId: Int?, val youtubeId: Int?) : MemoryKey(arrayOf(releaseId, youtubeId))