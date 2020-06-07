package anilibria.tv.cache.impl.memory.keys

import anilibria.tv.cache.impl.common.amazing.MemoryKey
import anilibria.tv.domain.entity.youtube.Youtube

data class ReleaseMemoryKey(val id: Int?, val code: String?) : MemoryKey(arrayOf(id, code))