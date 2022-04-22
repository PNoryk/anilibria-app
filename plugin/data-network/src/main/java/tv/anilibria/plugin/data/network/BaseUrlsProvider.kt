package tv.anilibria.plugin.data.network

import tv.anilibria.core.types.BaseUrl

interface BaseUrlsProvider {
    val widgetsSite: BaseUrl
    val site: BaseUrl
    val baseImages: BaseUrl
    val base: BaseUrl
    val api: BaseUrl
}