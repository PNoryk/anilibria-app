package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.entity.menu.LinkMenu
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.LinkMenuConverter
import ru.radiationx.data.api.service.MenuService
import toothpick.InjectConstructor

@InjectConstructor
class MenuApiDataSource(
    private val menuService: MenuService,
    private val menuConverter: LinkMenuConverter
) {

    fun getList(): Single<List<LinkMenu>> = menuService
        .getList(mapOf("query" to "link_menu"))
        .handleApiResponse()
        .map {
            it.map { menuResponse ->
                menuConverter.toDomain(menuResponse)
            }
        }
}