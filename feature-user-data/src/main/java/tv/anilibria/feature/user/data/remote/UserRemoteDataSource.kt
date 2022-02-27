package tv.anilibria.feature.user.data.remote

import tv.anilibria.feature.user.data.toDomain
import tv.anilibria.module.domain.entity.other.User
import tv.anilibria.plugin.data.network.NetworkWrapper
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse
import javax.inject.Inject

/**
 * Created by radiationx on 30.12.17.
 */
class UserRemoteDataSource @Inject constructor(
    private val userApi: NetworkWrapper<UserApi>
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