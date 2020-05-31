package ru.radiationx.data.api.datasource.impl

import io.reactivex.Single
import anilibria.tv.domain.entity.menu.LinkMenu
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.LinkMenuConverter
import ru.radiationx.data.api.datasource.MenuApiDataSource
import ru.radiationx.data.api.service.MenuService
import toothpick.InjectConstructor

@InjectConstructor
class MenuApiDataSourceImpl(
    private val menuService: MenuService,
    private val menuConverter: LinkMenuConverter
) : MenuApiDataSource {

    override fun getList(): Single<List<LinkMenu>> = menuService
        .getList(mapOf("query" to "link_menu"))
        .handleApiResponse()
        .map {
            it.map { menuResponse ->
                menuConverter.toDomain(menuResponse)
            }
        }
}