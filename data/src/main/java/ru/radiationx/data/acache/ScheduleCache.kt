package ru.radiationx.data.acache

import ru.radiationx.data.acache.common.ReadWriteCache
import ru.radiationx.data.adomain.entity.relative.ScheduleDayRelative

interface ScheduleCache : ReadWriteCache<ScheduleDayRelative>