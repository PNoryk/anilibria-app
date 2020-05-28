package ru.radiationx.data.acache

import ru.radiationx.data.acache.common.ReadWriteCache
import ru.radiationx.data.acache.common.ReadableCache
import ru.radiationx.data.adomain.entity.relative.FavoriteRelative
import ru.radiationx.data.adomain.entity.release.Episode

interface FavoriteCache : ReadWriteCache<FavoriteRelative>