package anilibria.tv.cache

import anilibria.tv.cache.common.ReadWriteCache
import anilibria.tv.domain.entity.relative.ReleaseHistoryRelative

interface ReleaseHistoryCache : ReadWriteCache<ReleaseHistoryRelative>