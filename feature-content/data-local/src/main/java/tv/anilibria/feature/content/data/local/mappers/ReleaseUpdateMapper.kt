package tv.anilibria.feature.content.data.local.mappers

import kotlinx.datetime.Instant
import tv.anilibria.feature.content.data.local.entity.ReleaseUpdateLocal
import tv.anilibria.module.domain.entity.ReleaseUpdate
import tv.anilibria.module.domain.entity.release.ReleaseId

fun ReleaseUpdate.toLocal() = ReleaseUpdateLocal(
    id = id.id,
    lastKnownUpdateAt = lastKnownUpdateAt.toEpochMilliseconds()
)

fun ReleaseUpdateLocal.toDomain() = ReleaseUpdate(
    id = ReleaseId(id = id),
    lastKnownUpdateAt = Instant.fromEpochMilliseconds(lastKnownUpdateAt)
)