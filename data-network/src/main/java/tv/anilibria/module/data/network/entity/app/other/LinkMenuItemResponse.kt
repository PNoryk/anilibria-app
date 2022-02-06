package tv.anilibria.module.data.network.entity.app.other

data class LinkMenuItemResponse(
    val title: String,
    val absoluteLink: String? = null,
    val sitePagePath: String? = null,
    val icon: String? = null
)