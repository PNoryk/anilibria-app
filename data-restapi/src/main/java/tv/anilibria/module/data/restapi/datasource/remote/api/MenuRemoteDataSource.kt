package tv.anilibria.module.data.restapi.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.restapi.entity.app.other.LinkMenuItemResponse
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.other.LinkMenuItem
import tv.anilibria.plugin.data.restapi.ApiNetworkClient
import tv.anilibria.plugin.data.restapi.ApiConfigProvider
import tv.anilibria.plugin.data.restapi.mapApiResponse
import javax.inject.Inject

class MenuRemoteDataSource @Inject constructor(
    private val apiClient: ApiNetworkClient,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) {

    fun getMenu(): Single<List<LinkMenuItem>> {
        val args = mapOf(
            "query" to "link_menu"
        )
        return apiClient
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<List<LinkMenuItemResponse>>(moshi)
            .map { items -> items.map { it.toDomain() } }
    }

}