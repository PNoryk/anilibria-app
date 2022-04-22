package tv.anilibria.feature.menu.data.remote

import toothpick.InjectConstructor
import tv.anilibria.feature.menu.data.domain.LinkMenuItem
import tv.anilibria.plugin.data.network.BaseUrlsProvider
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse

@InjectConstructor
class MenuRemoteDataSource(
    private val otherApi: MenuApiWrapper,
    private val urlsProvider: BaseUrlsProvider
) {

    suspend fun getMenu(): List<LinkMenuItem> {
        val args = formBodyOf(
            "query" to "link_menu"
        )
        return otherApi.proxy()
            .getMenu(urlsProvider.api.value, args)
            .handleApiResponse()
            .map { it.toDomain() }
    }

}