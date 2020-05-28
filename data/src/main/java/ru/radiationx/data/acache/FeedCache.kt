package ru.radiationx.data.acache

import ru.radiationx.data.acache.common.ReadWriteCache
import ru.radiationx.data.acache.common.ReadableCache
import ru.radiationx.data.adomain.entity.relative.FeedRelative
import ru.radiationx.data.adomain.entity.release.Episode

interface FeedCache : ReadWriteCache<FeedRelative>