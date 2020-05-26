package ru.radiationx.data.adb.datasource.converters

import ru.radiationx.data.adb.episode.EpisodeDb
import ru.radiationx.data.adomain.release.Episode
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeConverter {

    fun toDomain(episodeDb: EpisodeDb) = Episode(
        id = episodeDb.id,
        title = episodeDb.title,
        sd = episodeDb.sd,
        hd = episodeDb.hd,
        fullhd = episodeDb.fullhd,
        srcSd = episodeDb.srcSd,
        srcHd = episodeDb.srcHd
    )

    fun toDb(releaseId: Int, episode: Episode) = EpisodeDb(
        releaseId = releaseId,
        id = episode.id,
        title = episode.title,
        sd = episode.sd,
        hd = episode.hd,
        fullhd = episode.fullhd,
        srcSd = episode.srcSd,
        srcHd = episode.srcHd
    )

    fun toDomain(items: List<EpisodeDb>) = items.map { toDomain(it) }

    fun toDb(items: List<Pair<Int, Episode>>) = items.map { toDb(it.first, it.second) }
}