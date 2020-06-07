package anilibria.tv.cache.impl.memory

import anilibria.tv.cache.impl.common.ListMemoryDataSource
import anilibria.tv.domain.entity.menu.LinkMenu
import toothpick.InjectConstructor

@InjectConstructor
class LinkMenuMemoryDataSource : ListMemoryDataSource<LinkMenu>()