package tv.anilibria.module.data.restapi.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.network.NetworkClient
import tv.anilibria.module.data.restapi.ApiClient
import tv.anilibria.module.data.restapi.datasource.remote.ApiConfigProvider
import tv.anilibria.module.data.restapi.datasource.remote.mapApiResponse
import tv.anilibria.module.data.restapi.entity.app.other.LinkMenuItemResponse
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.other.LinkMenuItem
import javax.inject.Inject

class MenuRemoteDataSourceImpl @Inject constructor(
    @ApiClient private val client: NetworkClient,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) {

    fun getMenu(): Single<List<LinkMenuItem>> {
        val args = mapOf(
            "query" to "link_menu"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<List<LinkMenuItemResponse>>(moshi)
            .map { items -> items.map { it.toDomain() } }
    }

}