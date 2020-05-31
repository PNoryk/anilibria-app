package anilibria.tv.storage.impl.converter

import anilibria.tv.domain.entity.menu.LinkMenu
import anilibria.tv.storage.impl.entity.LinkMenuStorage
import toothpick.InjectConstructor

@InjectConstructor
class LinkMenuConverter {

    fun toDomain(source: LinkMenuStorage) = LinkMenu(
        title = source.title,
        absoluteLink = source.absoluteLink,
        sitePagePath = source.sitePagePath,
        icon = source.icon
    )

    fun toStorage(source: LinkMenu) = LinkMenuStorage(
        title = source.title,
        absoluteLink = source.absoluteLink,
        sitePagePath = source.sitePagePath,
        icon = source.icon
    )

    fun toDomain(source: List<LinkMenuStorage>) = source.map { toDomain(it) }

    fun toStorage(source: List<LinkMenu>) = source.map { toStorage(it) }
}