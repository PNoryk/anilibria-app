package anilibria.tv.cache.impl.memory

import anilibria.tv.cache.impl.common.ListMemoryDataSource
import toothpick.InjectConstructor

@InjectConstructor
class GenreMemoryDataSource : ListMemoryDataSource<String>()