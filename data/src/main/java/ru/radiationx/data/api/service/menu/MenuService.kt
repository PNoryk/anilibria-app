package ru.radiationx.data.api.service.menu

import io.reactivex.Single
import ru.radiationx.data.api.remote.LinkMenuResponse
import ru.radiationx.data.api.common.handleApiResponse

class MenuService(
    private val menuApi: MenuApi
) {

    fun getList(): Single<List<LinkMenuResponse>> = menuApi
        .getList(mapOf("query" to "link_menu"))
        .handleApiResponse()
}