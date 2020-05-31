package anilibria.tv.api.impl.datasource

import io.reactivex.Single
import anilibria.tv.domain.entity.user.User
import anilibria.tv.api.impl.common.handleApiResponse
import anilibria.tv.api.impl.converter.UserConverter
import anilibria.tv.api.UserApiDataSource
import anilibria.tv.api.impl.service.UserService
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