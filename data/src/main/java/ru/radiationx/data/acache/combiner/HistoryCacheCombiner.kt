package ru.radiationx.data.acache.combiner

import ru.radiationx.data.acache.common.ReadWriteCache
import ru.radiationx.data.adomain.entity.history.HistoryItem

interface HistoryCacheCombiner : ReadWriteCache<HistoryItem>