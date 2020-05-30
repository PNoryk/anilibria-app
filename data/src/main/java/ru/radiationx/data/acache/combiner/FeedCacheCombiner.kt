package ru.radiationx.data.acache.combiner

import ru.radiationx.data.acache.common.ReadWriteCache
import ru.radiationx.data.adomain.entity.feed.Feed

interface FeedCacheCombiner : ReadWriteCache<Feed>