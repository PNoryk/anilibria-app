package tv.anilibria.feature.menu.data.remote

import toothpick.InjectConstructor
import tv.anilibria.feature.menu.data.domain.LinkMenuItem
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse

@InjectConstructor
class MenuRemoteDataSource(
    private val otherApi: MenuApiWrapper
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