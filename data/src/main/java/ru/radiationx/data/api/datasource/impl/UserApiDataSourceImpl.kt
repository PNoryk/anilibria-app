package ru.radiationx.data.api.datasource.impl

import io.reactivex.Single
import ru.radiationx.data.adomain.entity.user.User
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.UserConverter
import ru.radiationx.data.api.datasource.UserApiDataSource
import ru.radiationx.data.api.service.UserService
import toothpick.InjectConstructor

@InjectConstructor
class UserApiDataSourceImpl(
    private val userService: UserService,
    private val userConverter: UserConverter
) : UserApiDataSource {

    override fun getSelf(): Single<User> = userService
        .getSelf(mapOf("query" to "user"))
        .handleApiResponse()
        .map { userConverter.toDomain(it) }
}