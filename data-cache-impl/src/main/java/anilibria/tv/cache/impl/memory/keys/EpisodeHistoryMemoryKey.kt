package anilibria.tv.cache.impl.memory.keys

import anilibria.tv.cache.impl.common.amazing.MemoryKey
import anilibria.tv.domain.entity.youtube.Youtube

data class EpisodeHistoryMemoryKey(val releaseId: Int, val id: Int?) : MemoryKey(arrayOf(releaseId, id))