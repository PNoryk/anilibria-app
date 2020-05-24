package ru.radiationx.data.api.service.menu

import io.reactivex.Single
import ru.radiationx.data.adomain.LinkMenu
import ru.radiationx.data.api.remote.LinkMenuResponse
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.LinkMenuConverter

class MenuService(
    private val menuApi: MenuApi,
    private val menuConverter: LinkMenuConverter
) {

    fun getList(): Single<List<LinkMenu>> = menuApi
        .getList(mapOf("query" to "link_menu"))
        .handleApiResponse()
        .map {
            it.map { menuResponse ->
                menuConverter.toDomain(menuResponse)
            }
        }
}