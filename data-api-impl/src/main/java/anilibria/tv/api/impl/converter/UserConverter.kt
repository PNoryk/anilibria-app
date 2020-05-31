package anilibria.tv.api.impl.converter

import anilibria.tv.domain.entity.user.User
import anilibria.tv.api.impl.entity.user.UserResponse
import toothpick.InjectConstructor

@InjectConstructor
class UserConverter {

    fun toDomain(response: UserResponse) = User(
        id = response.id,
        login = response.login,
        avatar = response.avatar
    )
}