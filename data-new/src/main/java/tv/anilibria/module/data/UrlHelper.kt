package tv.anilibria.module.data

import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.core.types.BaseUrl
import tv.anilibria.core.types.RelativeUrl

class Urls(
    val widgetsSite: BaseUrl,
    val site: BaseUrl,
    val baseImages: BaseUrl,
    val base: BaseUrl,
)

class UrlHelper(
    private val urls: Urls
) {

    fun makeWidget(url: RelativeUrl?): AbsoluteUrl? {
        return url?.let { AbsoluteUrl(urls.widgetsSite.value + it.value) }
    }

    fun makeSite(url: RelativeUrl?): AbsoluteUrl? {
        return url?.let { AbsoluteUrl(urls.site.value + it.value) }
    }

    fun makeBase(url: RelativeUrl?): AbsoluteUrl? {
        return url?.let { AbsoluteUrl(urls.base.value + it.value) }
    }

    fun makeMedia(url: RelativeUrl?): AbsoluteUrl? {
        return url?.let { AbsoluteUrl(urls.baseImages.value + it.value) }
    }
}
