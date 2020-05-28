package ru.radiationx.data.acache.combiner

import ru.radiationx.data.acache.common.ReadWriteCache
import ru.radiationx.data.acache.common.ReadableCache
import ru.radiationx.data.adomain.entity.release.Episode
import ru.radiationx.data.adomain.entity.release.Release
import ru.radiationx.data.adomain.entity.schedule.ScheduleDay

interface ScheduleCacheCombiner : ReadWriteCache<ScheduleDay>