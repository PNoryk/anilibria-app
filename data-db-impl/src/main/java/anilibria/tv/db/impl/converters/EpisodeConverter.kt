package anilibria.tv.db.impl.converters

import anilibria.tv.db.impl.entity.episode.EpisodeDb
import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.domain.entity.episode.Episode
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeConverter {

    fun toDbKey(releaseId: Int, episodeId: Int?): String = "${releaseId}_$episodeId"

    fun toDbKey(key: EpisodeKey): String = toDbKey(key.releaseId, key.id)

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

    fun toDbKey(keys: List<EpisodeKey>) = keys.map { toDbKey(it) }

    fun toDomain(source: List<EpisodeDb>) = source.map { toDomain(it) }

    fun toDb(source: List<Episode>) = source.map { toDb(it) }
}