package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.menu.LinkMenu
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.LinkMenuConverter
import ru.radiationx.data.api.service.MenuApi
import toothpick.InjectConstructor

@InjectConstructor
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