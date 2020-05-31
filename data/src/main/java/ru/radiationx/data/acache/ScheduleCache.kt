package ru.radiationx.data.acache

import ru.radiationx.data.acache.common.ReadWriteCache
import anilibria.tv.domain.entity.relative.ScheduleDayRelative

interface ScheduleCache : ReadWriteCache<ScheduleDayRelative>