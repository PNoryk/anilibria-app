package anilibria.tv.domain.entity.common.keys

import anilibria.tv.domain.entity.common.MemoryKey

data class TorrentKey(val releaseId: Int, val id: Int?) : MemoryKey(arrayOf(releaseId, id))