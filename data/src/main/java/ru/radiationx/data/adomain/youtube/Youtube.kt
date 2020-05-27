package ru.radiationx.data.adomain.youtube

import java.util.*


data class Youtube(
    val id: Int,
    val title: String?,
    val image: String?,
    val vid: String?,
    val views: Int,
    val comments: Int,
    val timestamp: Date
)