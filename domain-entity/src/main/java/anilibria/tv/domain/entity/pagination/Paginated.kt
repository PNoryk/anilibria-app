package anilibria.tv.domain.entity.pagination


data class Paginated<T>(
    val items: List<T>,
    val pagination: Pagination
)