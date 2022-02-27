package tv.anilibria.feature.user.data.local

import tv.anilibria.core.types.asRelativeUrl
import tv.anilibria.feature.user.data.domain.User
import tv.anilibria.feature.user.data.domain.UserId

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