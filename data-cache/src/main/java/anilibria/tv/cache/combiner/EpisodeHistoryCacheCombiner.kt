package anilibria.tv.cache.combiner

import anilibria.tv.cache.common.ReadWriteCache
import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.domain.entity.history.EpisodeHistory

interface EpisodeHistoryCacheCombiner : ReadWriteCache<EpisodeKey, EpisodeHistory>