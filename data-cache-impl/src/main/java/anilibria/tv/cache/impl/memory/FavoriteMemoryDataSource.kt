package anilibria.tv.cache.impl.memory

import anilibria.tv.cache.impl.common.amazing.AmazingMemoryDataSource
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.relative.FavoriteRelative
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteMemoryDataSource : AmazingMemoryDataSource<ReleaseKey, FavoriteRelative>()