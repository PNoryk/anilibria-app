package ru.radiationx.data.adb.converters

import ru.radiationx.data.adb.entity.episode.EpisodeDb
import ru.radiationx.data.adomain.entity.release.Episode
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeConverter {

    fun toDbKey(releaseId: Int, episodeId: Int): String = "${releaseId}_$episodeId"

    fun toDomain(source: EpisodeDb) = Episode(
        releaseId = source.releaseId,
        id = source.id,
        title = source.title,
        sd = source.sd,
        hd = source.hd,
        fullhd = source.fullhd,
        srcSd = source.srcSd,
        srcHd = source.srcHd
    )

    fun toDb(source: Episode) = EpisodeDb(
        key = toDbKey(source.releaseId, source.id),
        releaseId = source.releaseId,
        id = source.id,
        title = source.title,
        sd = source.sd,
        hd = source.hd,
        fullhd = source.fullhd,
        srcSd = source.srcSd,
        srcHd = source.srcHd
    )

    fun toDbKey(ids: List<Pair<Int, Int>>) = ids.map { toDbKey(it.first, it.second) }

    fun toDomain(source: List<EpisodeDb>) = source.map { toDomain(it) }

    fun toDb(source: List<Episode>) = source.map { toDb(it) }
}