package tv.anilibria.module.data.local.mappers

import kotlinx.datetime.Instant
import tv.anilibria.module.data.local.entity.ReleaseVisitLocal
import tv.anilibria.module.domain.entity.ReleaseVisit
import tv.anilibria.module.domain.entity.release.ReleaseId

fun ReleaseVisit.toLocal() = ReleaseVisitLocal(
    id = id.id,
    lastKnownUpdateAt = lastKnownUpdateAt.toEpochMilliseconds(),
    lastOpenAt = lastOpenAt.toEpochMilliseconds()
)

fun ReleaseVisitLocal.toDomain() = ReleaseVisit(
    id = ReleaseId(id = id),
    lastKnownUpdateAt = Instant.fromEpochMilliseconds(lastKnownUpdateAt),
    lastOpenAt = Instant.fromEpochMilliseconds(lastOpenAt)
)