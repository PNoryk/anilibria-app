package anilibria.tv.cache

import anilibria.tv.cache.common.ReadWriteCache
import anilibria.tv.domain.entity.relative.FeedRelative

interface FeedCache : ReadWriteCache<FeedRelative>