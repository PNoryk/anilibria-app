package tv.anilibria.feature.user.data.remote

import tv.anilibria.core.types.asRelativeUrl
import tv.anilibria.feature.user.data.domain.User
import tv.anilibria.feature.user.data.domain.UserId
import tv.anilibria.feature.user.data.remote.UserResponse

fun UserResponse.toDomain() = User(
    id = UserId(id),
    avatar = avatar?.asRelativeUrl(),
    login = login
)