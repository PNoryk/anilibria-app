package ru.radiationx.data.adomain.pagination


data class Pagination(
    val page: Int,
    val perPage: Int,
    val allPages: Int,
    val allItems: Int
)