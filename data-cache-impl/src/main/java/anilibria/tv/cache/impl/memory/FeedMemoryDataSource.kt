package anilibria.tv.cache.impl.memory

import anilibria.tv.cache.impl.common.amazing.AmazingMemoryDataSource
import anilibria.tv.domain.entity.common.keys.FeedKey
import anilibria.tv.domain.entity.relative.FeedRelative
import toothpick.InjectConstructor

@InjectConstructor
class FeedMemoryDataSource : AmazingMemoryDataSource<FeedKey, FeedRelative>()