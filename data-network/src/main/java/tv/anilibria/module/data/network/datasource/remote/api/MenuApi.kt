package tv.anilibria.module.data.network.datasource.remote.api

import io.reactivex.Single
import org.json.JSONArray
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.ApiResponse
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfig
import tv.anilibria.module.data.network.datasource.remote.parsers.MenuParser
import tv.anilibria.module.data.network.entity.app.other.LinkMenuItemResponse
import javax.inject.Inject

class MenuApi @Inject constructor(
        @ApiClient private val client: IClient,
        private val menuParse: MenuParser,
        private val apiConfig: ApiConfig
) {

    fun getMenu(): Single<List<LinkMenuItemResponse>> {
        val args: Map<String, String> = mapOf(
                "query" to "link_menu"
        )
        return client.post(apiConfig.apiUrl, args)
                .compose(ApiResponse.fetchResult<JSONArray>())
                .map { menuParse.parse(it) }
    }

}