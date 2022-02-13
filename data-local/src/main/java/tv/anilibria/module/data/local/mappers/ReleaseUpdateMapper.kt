package tv.anilibria.module.data.local.mappers

import kotlinx.datetime.Instant
import tv.anilibria.module.data.local.entity.ReleaseUpdateLocal
import tv.anilibria.module.data.local.entity.ReleaseVisitLocal
import tv.anilibria.module.domain.entity.ReleaseUpdate
import tv.anilibria.module.domain.entity.ReleaseVisit
import tv.anilibria.module.domain.entity.release.ReleaseId

fun ReleaseUpdate.toLocal() = ReleaseUpdateLocal(
    id = id.id,
    lastKnownUpdateAt = lastKnownUpdateAt.toEpochMilliseconds()
)

fun ReleaseUpdateLocal.toDomain() = ReleaseUpdate(
    id = ReleaseId(id = id),
    lastKnownUpdateAt = Instant.fromEpochMilliseconds(lastKnownUpdateAt)
)