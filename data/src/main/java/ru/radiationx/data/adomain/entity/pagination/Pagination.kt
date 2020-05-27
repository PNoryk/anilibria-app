package ru.radiationx.data.adomain.entity.pagination


data class Pagination(
    val page: Int,
    val perPage: Int,
    val allPages: Int,
    val allItems: Int
)