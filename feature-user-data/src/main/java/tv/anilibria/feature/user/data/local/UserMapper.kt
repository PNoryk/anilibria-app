package tv.anilibria.feature.user.data.local

import tv.anilibria.core.types.asRelativeUrl
import tv.anilibria.module.domain.entity.other.User
import tv.anilibria.module.domain.entity.other.UserId

fun User.toLocal() = UserLocal(
    id = id.id,
    avatar = avatar?.value,
    login = login
)

fun UserLocal.toDomain() = User(
    id = UserId(id = id),
    avatar = avatar?.asRelativeUrl(),
    login = login
)