package anilibria.tv.db.impl.converters

import anilibria.tv.db.impl.entity.torrent.TorrentDb
import anilibria.tv.domain.entity.common.keys.TorrentKey
import anilibria.tv.domain.entity.torrent.Torrent
import toothpick.InjectConstructor

@InjectConstructor
class TorrentConverter {

    fun toDbKey(releaseId: Int, torrentId: Int?): String = "${releaseId}_$torrentId"

    fun toDbKey(key: TorrentKey): String = toDbKey(key.releaseId, key.id)

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
        key = toDbKey(source.releaseId, source.id),
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

    fun toDbKey(ids: List<TorrentKey>) = ids.map { toDbKey(it) }

    fun toDomain(source: List<TorrentDb>) = source.map { toDomain(it) }

    fun toDb(source: List<Torrent>) = source.map { toDb(it) }
}