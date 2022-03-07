package tv.anilibria.feature.content.data.local.mappers

import kotlinx.datetime.Instant
import tv.anilibria.feature.content.data.local.entity.ReleaseUpdateLocal
import tv.anilibria.feature.content.types.ReleaseUpdate
import tv.anilibria.feature.content.types.release.ReleaseId

fun ReleaseUpdate.toLocal() = ReleaseUpdateLocal(
    id = id.id,
    lastKnownUpdateAt = lastKnownUpdateAt.toEpochMilliseconds()
)

fun ReleaseUpdateLocal.toDomain() = ReleaseUpdate(
    id = ReleaseId(id = id),
    lastKnownUpdateAt = Instant.fromEpochMilliseconds(lastKnownUpdateAt)
)