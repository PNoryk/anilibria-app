package ru.radiationx.data.acache.combiner

import ru.radiationx.data.acache.common.ReadWriteCache
import anilibria.tv.domain.entity.schedule.ScheduleDay

interface ScheduleCacheCombiner : ReadWriteCache<ScheduleDay>