package anilibria.tv.cache.combiner

import anilibria.tv.cache.common.ReadWriteCache
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.history.ReleaseHistory

interface ReleaseHistoryCacheCombiner : ReadWriteCache<ReleaseKey, ReleaseHistory>