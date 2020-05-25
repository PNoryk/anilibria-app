package ru.radiationx.data.api.service.user

import io.reactivex.Single
import ru.radiationx.data.adomain.User
import ru.radiationx.data.api.remote.UserResponse
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.UserConverter
import toothpick.InjectConstructor

@InjectConstructor
class UserService(
    private val userApi: UserApi,
    private val userConverter: UserConverter
) {

    fun getSelf(): Single<User> = userApi
        .getSelf(mapOf("query" to "user"))
        .handleApiResponse()
        .map { userConverter.toDomain(it) }
}