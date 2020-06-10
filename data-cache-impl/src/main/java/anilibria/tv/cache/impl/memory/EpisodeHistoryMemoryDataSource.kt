package anilibria.tv.cache.impl.memory

import anilibria.tv.cache.impl.common.amazing.AmazingMemoryDataSource
import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.domain.entity.relative.EpisodeHistoryRelative
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeHistoryMemoryDataSource : AmazingMemoryDataSource<EpisodeKey, EpisodeHistoryRelative>()