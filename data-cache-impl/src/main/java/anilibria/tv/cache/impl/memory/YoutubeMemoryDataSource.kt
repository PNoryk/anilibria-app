package anilibria.tv.cache.impl.memory

import anilibria.tv.cache.impl.common.amazing.AmazingMemoryDataSource
import anilibria.tv.domain.entity.common.keys.YoutubeKey
import anilibria.tv.domain.entity.youtube.Youtube
import toothpick.InjectConstructor

@InjectConstructor
class YoutubeMemoryDataSource : AmazingMemoryDataSource<YoutubeKey, Youtube>()