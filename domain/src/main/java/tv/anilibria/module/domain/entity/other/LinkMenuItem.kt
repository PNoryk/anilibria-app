package tv.anilibria.module.domain.entity.other

data class LinkMenuItem(
    val title: String,
    val absoluteLink: String? = null,
    val sitePagePath: String? = null,
    val icon: String? = null
)