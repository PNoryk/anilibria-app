package ru.radiationx.data.adomain.entity.pagination


data class Paginated<T>(
    val items: List<T>,
    val pagination: Pagination
)