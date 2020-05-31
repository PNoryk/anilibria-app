package anilibria.tv.cache.combiner

import anilibria.tv.cache.common.ReadWriteCache
import anilibria.tv.domain.entity.history.HistoryItem

interface HistoryCacheCombiner : ReadWriteCache<HistoryItem>