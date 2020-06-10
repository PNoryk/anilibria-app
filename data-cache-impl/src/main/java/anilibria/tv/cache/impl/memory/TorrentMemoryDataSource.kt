package anilibria.tv.cache.impl.memory

import anilibria.tv.cache.impl.common.amazing.AmazingMemoryDataSource
import anilibria.tv.domain.entity.common.keys.TorrentKey
import anilibria.tv.domain.entity.torrent.Torrent
import toothpick.InjectConstructor

@InjectConstructor
class TorrentMemoryDataSource : AmazingMemoryDataSource<TorrentKey, Torrent>()