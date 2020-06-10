package anilibria.tv.domain.entity.common.keys

import anilibria.tv.domain.entity.common.MemoryKey

data class YoutubeKey(val id: Int) : MemoryKey(arrayOf(id))