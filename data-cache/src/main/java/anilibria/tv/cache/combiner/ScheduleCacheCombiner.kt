package anilibria.tv.cache.combiner

import anilibria.tv.cache.common.ReadWriteCache
import anilibria.tv.domain.entity.schedule.ScheduleDay

interface ScheduleCacheCombiner : ReadWriteCache<ScheduleDay>