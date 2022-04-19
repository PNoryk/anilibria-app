package tv.anilibria.feature.user.data.remote

import toothpick.InjectConstructor
import tv.anilibria.feature.user.data.domain.User
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse

@InjectConstructor
class UserRemoteDataSource(
    private val userApi: UserApiWrapper
) {

    suspend fun loadUser(): User {
        val args = formBodyOf(
            "query" to "user"
        )
        return userApi.proxy()
            .getUser(args)
            .handleApiResponse()
            .toDomain()
    }
}