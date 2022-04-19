package tv.anilibria.feature.menu.data.remote

import tv.anilibria.feature.menu.data.domain.LinkMenuItem
import tv.anilibria.plugin.data.network.ApiWrapper
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse
import javax.inject.Inject

class MenuRemoteDataSource @Inject constructor(
    private val otherApi: ApiWrapper<MenuApi>
) {

    suspend fun getMenu(): List<LinkMenuItem> {
        val args = formBodyOf(
            "query" to "link_menu"
        )
        return otherApi.proxy()
            .getMenu(args)
            .handleApiResponse()
            .map { it.toDomain() }
    }

}