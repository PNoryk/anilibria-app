package ru.radiationx.data.acache.combiner

import ru.radiationx.data.acache.common.ReadWriteCache
import anilibria.tv.domain.entity.history.HistoryItem

interface HistoryCacheCombiner : ReadWriteCache<HistoryItem>