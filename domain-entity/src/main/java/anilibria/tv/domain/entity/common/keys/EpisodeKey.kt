package anilibria.tv.domain.entity.common.keys

import anilibria.tv.domain.entity.common.MemoryKey

data class EpisodeKey(val releaseId: Int, val id: Int?) : MemoryKey(arrayOf(releaseId, id))