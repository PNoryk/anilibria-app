package anilibria.tv.cache.combiner

import anilibria.tv.cache.common.ReadWriteCache
import anilibria.tv.domain.entity.release.ReleaseHistory

interface ReleaseHistoryCacheCombiner : ReadWriteCache<ReleaseHistory>