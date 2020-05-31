package anilibria.tv.cache.combiner

import anilibria.tv.cache.common.ReadWriteCache
import anilibria.tv.domain.entity.history.EpisodeHistory
import anilibria.tv.domain.entity.history.ReleaseHistory

interface EpisodeHistoryCacheCombiner : ReadWriteCache<EpisodeHistory>