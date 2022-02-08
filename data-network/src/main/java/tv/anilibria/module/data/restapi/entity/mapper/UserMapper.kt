package tv.anilibria.module.data.restapi.entity.mapper

import tv.anilibria.module.data.restapi.entity.app.other.UserResponse
import tv.anilibria.module.domain.entity.common.asRelativeUrl
import tv.anilibria.module.domain.entity.other.User
import tv.anilibria.module.domain.entity.other.UserId

fun UserResponse.toDomain() = User(
    id = UserId(id),
    avatar = avatar?.asRelativeUrl(),
    login = login
)