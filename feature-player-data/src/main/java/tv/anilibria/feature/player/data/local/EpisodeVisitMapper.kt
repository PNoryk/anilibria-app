package tv.anilibria.feature.player.data.local

import kotlinx.datetime.Instant
import tv.anilibria.feature.player.data.domain.EpisodeVisit
import tv.anilibria.feature.domain.entity.release.EpisodeId
import tv.anilibria.feature.domain.entity.release.ReleaseId

fun EpisodeVisit.toLocal() = EpisodeVisitLocal(
    id = id.id,
    releaseId = id.releaseId.id,
    playerSeek = playerSeek,
    lastOpenAt = lastOpenAt?.toEpochMilliseconds()
)

fun EpisodeVisitLocal.toDomain() = EpisodeVisit(
    id = EpisodeId(id = id, releaseId = ReleaseId(releaseId)),
    playerSeek = playerSeek,
    lastOpenAt = lastOpenAt?.let { Instant.fromEpochMilliseconds(it) }
)