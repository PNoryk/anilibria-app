package anilibria.tv.cache.impl.memory.keys

import anilibria.tv.cache.impl.common.amazing.MemoryKey
import anilibria.tv.domain.entity.youtube.Youtube

data class FeedMemoryKey(val releaseId: Int?, val youtubeId: Int?) : MemoryKey(arrayOf(releaseId, youtubeId))