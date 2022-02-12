package tv.anilibria.module.data.restapi.datasource.remote.api

import tv.anilibria.module.data.restapi.datasource.remote.retrofit.UserApi
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.other.User
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.ApiWrapper
import tv.anilibria.plugin.data.restapi.handleApiResponse
import javax.inject.Inject

/**
 * Created by radiationx on 30.12.17.
 */
class UserRemoteDataSource @Inject constructor(
    private val userApi: ApiWrapper<UserApi>
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