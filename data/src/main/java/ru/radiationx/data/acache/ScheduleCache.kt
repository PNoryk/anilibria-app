package ru.radiationx.data.acache

import ru.radiationx.data.acache.common.ReadWriteCache
import ru.radiationx.data.acache.common.ReadableCache
import ru.radiationx.data.adomain.entity.relative.ScheduleDayRelative
import ru.radiationx.data.adomain.entity.release.Episode

interface ScheduleCache : ReadWriteCache<ScheduleDayRelative>