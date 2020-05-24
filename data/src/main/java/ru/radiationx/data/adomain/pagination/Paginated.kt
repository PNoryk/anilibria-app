package ru.radiationx.data.adomain.pagination


data class Paginated<T>(
    val items: List<T>,
    val pagination: Pagination
)