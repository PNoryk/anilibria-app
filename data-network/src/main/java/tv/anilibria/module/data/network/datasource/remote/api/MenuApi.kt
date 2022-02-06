package tv.anilibria.module.data.network.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfigProvider
import tv.anilibria.module.data.network.datasource.remote.mapApiResponse
import tv.anilibria.module.data.network.datasource.remote.parsers.MenuParser
import tv.anilibria.module.data.network.entity.app.other.LinkMenuItemResponse
import javax.inject.Inject

class MenuApi @Inject constructor(
    @ApiClient private val client: IClient,
    private val menuParse: MenuParser,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) {

    fun getMenu(): Single<List<LinkMenuItemResponse>> {
        val args: Map<String, String> = mapOf(
            "query" to "link_menu"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse(moshi)
    }

}