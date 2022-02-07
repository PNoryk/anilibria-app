package tv.anilibria.module.data.network.entity.mapper

import tv.anilibria.module.data.network.entity.app.other.UserResponse
import tv.anilibria.module.domain.entity.common.asRelativeUrl
import tv.anilibria.module.domain.entity.other.User

fun UserResponse.toDomain() = User(
    id = id,
    avatar = avatar?.asRelativeUrl(),
    login = login
)