package ru.radiationx.data.api.converter

import ru.radiationx.data.adomain.user.User
import ru.radiationx.data.api.remote.user.UserResponse
import ru.radiationx.data.datasource.remote.address.ApiConfig
import toothpick.InjectConstructor

@InjectConstructor
class UserConverter(
    private val apiConfig: ApiConfig
) {

    fun toDomain(response: UserResponse) = User(
        id = response.id,
        login = response.login,
        avatar = response.avatar?.let { "${apiConfig.baseImagesUrl}$it" }
    )
}