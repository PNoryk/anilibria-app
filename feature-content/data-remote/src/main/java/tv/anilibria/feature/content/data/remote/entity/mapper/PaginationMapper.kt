package tv.anilibria.feature.content.data.remote.entity.mapper

import tv.anilibria.feature.content.data.remote.entity.app.PageMetaResponse
import tv.anilibria.feature.content.data.remote.entity.app.PageResponse
import tv.anilibria.feature.content.types.Page
import tv.anilibria.feature.content.types.PageMeta

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