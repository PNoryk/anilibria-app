package tv.anilibria.module.data.restapi.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.network.NetworkClient
import tv.anilibria.module.data.restapi.ApiClient
import tv.anilibria.module.data.restapi.datasource.remote.ApiConfigProvider
import tv.anilibria.module.data.restapi.datasource.remote.mapApiResponse
import tv.anilibria.module.data.restapi.entity.app.other.UserResponse
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.other.User
import javax.inject.Inject

/**
 * Created by radiationx on 30.12.17.
 */
class UserRemoteDataSourceImpl @Inject constructor(
    @ApiClient private val client: NetworkClient,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) {

    fun loadUser(): Single<User> {
        val args = mapOf(
            "query" to "user"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<UserResponse>(moshi)
            .map { it.toDomain() }
    }
}