package anilibria.tv.api.impl.datasource

import io.reactivex.Single
import anilibria.tv.domain.entity.menu.LinkMenu
import anilibria.tv.api.impl.common.handleApiResponse
import anilibria.tv.api.impl.converter.LinkMenuConverter
import anilibria.tv.api.MenuApiDataSource
import anilibria.tv.api.impl.service.MenuService
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