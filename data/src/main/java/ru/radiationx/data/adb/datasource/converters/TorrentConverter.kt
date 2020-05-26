package ru.radiationx.data.adb.datasource.converters

import ru.radiationx.data.adb.torrent.TorrentDb
import ru.radiationx.data.adomain.release.Torrent
import toothpick.InjectConstructor

@InjectConstructor
class TorrentConverter {

    fun toDomain(torrentDb: TorrentDb) = Torrent(
        id = torrentDb.id,
        hash = torrentDb.hash,
        leechers = torrentDb.leechers,
        seeders = torrentDb.seeders,
        completed = torrentDb.completed,
        quality = torrentDb.quality,
        series = torrentDb.series,
        size = torrentDb.size,
        time = torrentDb.time,
        url = torrentDb.url
    )

    fun toDb(releaseId: Int, torrent: Torrent): TorrentDb = TorrentDb(
        releaseId = releaseId,
        id = torrent.id,
        hash = torrent.hash,
        leechers = torrent.leechers,
        seeders = torrent.seeders,
        completed = torrent.completed,
        quality = torrent.quality,
        series = torrent.series,
        size = torrent.size,
        time = torrent.time,
        url = torrent.url
    )


    fun toDomain(items: List<TorrentDb>) = items.map { toDomain(it) }

    fun toDb(items: List<Pair<Int, Torrent>>) = items.map { toDb(it.first, it.second) }
}