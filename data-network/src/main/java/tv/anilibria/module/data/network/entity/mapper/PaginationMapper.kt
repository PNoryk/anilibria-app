package tv.anilibria.module.data.network.entity.mapper

import tv.anilibria.module.data.network.entity.app.PageMetaResponse
import tv.anilibria.module.data.network.entity.app.PageResponse
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.PageMeta

fun <T, R> PageResponse<T>.toDomain(transform: (T) -> R) = Page(
    items = items.map(transform),
    meta = meta.toDomain()
)

fun PageMetaResponse.toDomain() = PageMeta(
    page = page,
    perPage = perPage,
    allPages = allPages,
    allItems = allItems
)