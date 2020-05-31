package ru.radiationx.data.acache

import ru.radiationx.data.acache.common.ReadWriteCache
import anilibria.tv.domain.entity.relative.FavoriteRelative

interface FavoriteCache : ReadWriteCache<FavoriteRelative>