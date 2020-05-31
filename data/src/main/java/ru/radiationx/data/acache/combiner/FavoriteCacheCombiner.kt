package ru.radiationx.data.acache.combiner

import ru.radiationx.data.acache.common.ReadWriteCache
import anilibria.tv.domain.entity.release.Release

interface FavoriteCacheCombiner : ReadWriteCache<Release>