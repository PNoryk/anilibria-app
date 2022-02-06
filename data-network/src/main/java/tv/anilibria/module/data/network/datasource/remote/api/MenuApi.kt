package tv.anilibria.module.data.network.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfigProvider
import tv.anilibria.module.data.network.datasource.remote.mapApiResponse
import tv.anilibria.module.data.network.entity.app.other.LinkMenuItemResponse
import tv.anilibria.module.data.network.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.other.LinkMenuItem
import javax.inject.Inject

class MenuApi @Inject constructor(
    @ApiClient private val client: IClient,
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