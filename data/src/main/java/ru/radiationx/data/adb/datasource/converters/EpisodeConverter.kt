package ru.radiationx.data.adb.datasource.converters

import ru.radiationx.data.adb.episode.EpisodeDb
import ru.radiationx.data.adomain.release.Episode
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeConverter {

    fun toDomain(source: EpisodeDb) = Episode(
        id = source.id,
        title = source.title,
        sd = source.sd,
        hd = source.hd,
        fullhd = source.fullhd,
        srcSd = source.srcSd,
        srcHd = source.srcHd
    )

    fun toDb(releaseId: Int, source: Episode) = EpisodeDb(
        releaseId = releaseId,
        id = source.id,
        title = source.title,
        sd = source.sd,
        hd = source.hd,
        fullhd = source.fullhd,
        srcSd = source.srcSd,
        srcHd = source.srcHd
    )

    fun toDomain(source: List<EpisodeDb>) = source.map { toDomain(it) }

    fun toDb(source: List<Pair<Int, Episode>>) = source.map { toDb(it.first, it.second) }
}