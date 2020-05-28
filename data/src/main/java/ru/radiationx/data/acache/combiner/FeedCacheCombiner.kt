package ru.radiationx.data.acache.combiner

import ru.radiationx.data.acache.common.ReadWriteCache
import ru.radiationx.data.acache.common.ReadableCache
import ru.radiationx.data.adomain.entity.feed.Feed
import ru.radiationx.data.adomain.entity.release.Episode
import ru.radiationx.data.adomain.entity.release.Release

interface FeedCacheCombiner : ReadWriteCache<Feed>