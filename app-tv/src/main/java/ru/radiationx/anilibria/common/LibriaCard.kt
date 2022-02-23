package ru.radiationx.anilibria.common

import tv.anilibria.core.types.AbsoluteUrl

data class LibriaCard(
    val id: Long,
    val title: String,
    val description: String,
    val image: AbsoluteUrl?,
    val type: Type
) {

    var rawData: Any? = null

    enum class Type {
        RELEASE,
        YOUTUBE
    }
}