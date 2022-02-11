package tv.anilibria.module.data.restapi.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.restapi.entity.app.other.UserResponse
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.other.User
import tv.anilibria.plugin.data.restapi.ApiNetworkClient
import tv.anilibria.plugin.data.restapi.ApiConfigProvider
import tv.anilibria.plugin.data.restapi.mapApiResponse
import javax.inject.Inject

/**
 * Created by radiationx on 30.12.17.
 */
class UserRemoteDataSource @Inject constructor(
    private val apiClient: ApiNetworkClient,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) {

    fun loadUser(): Single<User> {
        val args = mapOf(
            "query" to "user"
        )
        return apiClient
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<UserResponse>(moshi)
            .map { it.toDomain() }
    }
}