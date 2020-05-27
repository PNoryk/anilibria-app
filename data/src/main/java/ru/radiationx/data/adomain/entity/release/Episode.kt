package ru.radiationx.data.adomain.entity.release


data class Episode(
    val id: Int,
    val title: String?,
    val sd: String?,
    val hd: String?,
    val fullhd: String?,
    val srcSd: String?,
    val srcHd: String?
)