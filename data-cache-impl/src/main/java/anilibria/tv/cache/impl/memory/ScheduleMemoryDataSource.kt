package anilibria.tv.cache.impl.memory

import anilibria.tv.cache.impl.common.amazing.AmazingMemoryDataSource
import anilibria.tv.domain.entity.common.keys.ScheduleKey
import anilibria.tv.domain.entity.relative.ScheduleDayRelative
import toothpick.InjectConstructor

@InjectConstructor
class ScheduleMemoryDataSource : AmazingMemoryDataSource<ScheduleKey, ScheduleDayRelative>()