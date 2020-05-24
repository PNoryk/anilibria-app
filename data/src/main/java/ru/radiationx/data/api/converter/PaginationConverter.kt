package ru.radiationx.data.api.converter

import ru.radiationx.data.adomain.pagination.Paginated
import ru.radiationx.data.adomain.pagination.Pagination
import ru.radiationx.data.api.common.pagination.PaginatedResponse
import ru.radiationx.data.api.common.pagination.PaginationResponse

class PaginationConverter {

    fun <T, R> toDomain(response: PaginatedResponse<T>, block: (T) -> R): Paginated<R> = Paginated(
        items = response.items.map(block),
        pagination = toDomain(response.pagination)
    )

    fun toDomain(response: PaginationResponse) = Pagination(
        page = response.page,
        perPage = response.perPage,
        allPages = response.allPages,
        allItems = response.allItems
    )
}