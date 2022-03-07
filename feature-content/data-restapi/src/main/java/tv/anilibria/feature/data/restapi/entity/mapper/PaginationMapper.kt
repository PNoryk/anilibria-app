package tv.anilibria.feature.data.restapi.entity.mapper

import tv.anilibria.feature.data.restapi.entity.app.PageMetaResponse
import tv.anilibria.feature.data.restapi.entity.app.PageResponse
import tv.anilibria.feature.domain.entity.Page
import tv.anilibria.feature.domain.entity.PageMeta

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