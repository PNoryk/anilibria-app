package anilibria.tv.domain.entity.common.keys

import anilibria.tv.domain.entity.common.MemoryKey

data class ScheduleKey(val dayId: Int) : MemoryKey(arrayOf(dayId))