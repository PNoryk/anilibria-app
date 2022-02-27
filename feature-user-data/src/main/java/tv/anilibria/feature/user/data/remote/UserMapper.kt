package tv.anilibria.feature.user.data

import tv.anilibria.core.types.asRelativeUrl
import tv.anilibria.feature.user.data.remote.UserResponse
import tv.anilibria.module.domain.entity.other.User
import tv.anilibria.module.domain.entity.other.UserId

fun UserResponse.toDomain() = User(
    id = UserId(id),
    avatar = avatar?.asRelativeUrl(),
    login = login
)