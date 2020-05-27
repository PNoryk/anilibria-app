package ru.radiationx.data.api.converter

import ru.radiationx.data.adomain.menu.LinkMenu
import ru.radiationx.data.api.entity.menu.LinkMenuResponse
import toothpick.InjectConstructor

@InjectConstructor
class LinkMenuConverter {

    fun toDomain(response: LinkMenuResponse) = LinkMenu(
        title = response.title,
        absoluteLink = response.absoluteLink,
        sitePagePath = response.sitePagePath,
        icon = response.icon
    )
}