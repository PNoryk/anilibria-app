package ru.radiationx.data.entity.app.other

@Deprecated("old data")
data class LinkMenuItem(
    val title: String,
    val absoluteLink: String? = null,
    val sitePagePath: String? = null,
    val icon: String? = null
)