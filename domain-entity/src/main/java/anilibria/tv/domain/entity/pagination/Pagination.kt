package anilibria.tv.domain.entity.pagination


data class Pagination(
    val page: Int,
    val perPage: Int,
    val allPages: Int,
    val allItems: Int
)