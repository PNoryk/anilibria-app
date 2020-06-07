package anilibria.tv.cache.impl.memory.keys

import anilibria.tv.cache.impl.common.amazing.MemoryKey
import anilibria.tv.domain.entity.youtube.Youtube

data class ScheduleMemoryKey(val dayId: Int) : MemoryKey(arrayOf(dayId))