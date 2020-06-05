package anilibria.tv.api.impl.converter

import anilibria.tv.domain.entity.pagination.Paginated
import anilibria.tv.domain.entity.pagination.Pagination
import anilibria.tv.api.impl.entity.pagination.PaginatedResponse
import anilibria.tv.api.impl.entity.pagination.PaginationResponse
import toothpick.InjectConstructor

@InjectConstructor
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