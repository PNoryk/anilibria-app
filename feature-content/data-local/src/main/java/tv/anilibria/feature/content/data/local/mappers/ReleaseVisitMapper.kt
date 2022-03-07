package tv.anilibria.feature.content.data.local.mappers

import kotlinx.datetime.Instant
import tv.anilibria.feature.content.data.local.entity.ReleaseVisitLocal
import tv.anilibria.module.domain.entity.ReleaseVisit
import tv.anilibria.module.domain.entity.release.ReleaseId

fun ReleaseVisit.toLocal() = ReleaseVisitLocal(
    id = id.id,
    lastOpenAt = lastOpenAt.toEpochMilliseconds()
)

fun ReleaseVisitLocal.toDomain() = ReleaseVisit(
    id = ReleaseId(id = id),
    lastOpenAt = Instant.fromEpochMilliseconds(lastOpenAt)
)