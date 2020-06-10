package anilibria.tv.cache.combiner

import anilibria.tv.cache.common.ReadWriteCache
import anilibria.tv.domain.entity.common.keys.FeedKey
import anilibria.tv.domain.entity.feed.Feed

interface FeedCacheCombiner : ReadWriteCache<FeedKey, Feed>