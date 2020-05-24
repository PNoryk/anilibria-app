package ru.radiationx.data.api.converter

import ru.radiationx.data.adomain.User
import ru.radiationx.data.api.remote.UserResponse


class UserConverter {

    fun toDomain(response: UserResponse) = User(
        id = response.id,
        login = response.login,
        avatar = response.avatar
    )
}