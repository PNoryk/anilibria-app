package ru.radiationx.data.api.converter

import ru.radiationx.data.adomain.user.User
import ru.radiationx.data.api.entity.user.UserResponse
import toothpick.InjectConstructor

@InjectConstructor
class UserConverter {

    fun toDomain(response: UserResponse) = User(
        id = response.id,
        login = response.login,
        avatar = response.avatar
    )
}