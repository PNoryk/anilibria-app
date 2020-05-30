package ru.radiationx.data.adb.converters

import ru.radiationx.data.adb.entity.torrent.TorrentDb
import ru.radiationx.data.adomain.entity.release.Torrent
import toothpick.InjectConstructor

@InjectConstructor
class TorrentConverter {

    fun toDomain(source: TorrentDb) = Torrent(
        releaseId = source.releaseId,
        id = source.id,
        hash = source.hash,
        leechers = source.leechers,
        seeders = source.seeders,
        completed = source.completed,
        quality = source.quality,
        series = source.series,
        size = source.size,
        time = source.time,
        url = source.url
    )

    fun toDb(source: Torrent): TorrentDb = TorrentDb(
        releaseId = source.releaseId,
        id = source.id,
        hash = source.hash,
        leechers = source.leechers,
        seeders = source.seeders,
        completed = source.completed,
        quality = source.quality,
        series = source.series,
        size = source.size,
        time = source.time,
        url = source.url
    )


    fun toDomain(source: List<TorrentDb>) = source.map { toDomain(it) }

    fun toDb(source: List<Torrent>) = source.map { toDb(it) }
}