package ru.radiationx.data.adomain

import java.util.*


data class YouTube(
    val id: Int,
    val title: String?,
    val image: String?,
    val vid: String?,
    val views: Int,
    val comments: Int,
    val timestamp: Date
)