package tv.anilibria.module.data.local.mappers

import kotlinx.datetime.Instant
import tv.anilibria.module.data.local.entity.EpisodeVisitLocal
import tv.anilibria.module.domain.entity.EpisodeVisit
import tv.anilibria.module.domain.entity.release.EpisodeId
import tv.anilibria.module.domain.entity.release.ReleaseId

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