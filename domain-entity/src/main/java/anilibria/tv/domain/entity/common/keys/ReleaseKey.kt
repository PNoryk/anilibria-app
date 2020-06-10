package anilibria.tv.domain.entity.common.keys

import anilibria.tv.domain.entity.common.MemoryKey

data class ReleaseKey(val id: Int) : MemoryKey(arrayOf(id))