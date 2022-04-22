package tv.anilibria.feature.content.data

import toothpick.InjectConstructor
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.plugin.data.network.BaseUrlsProvider

@InjectConstructor
class BaseUrlHelper(
    private val urlsProvider: BaseUrlsProvider
) {

    fun makeWidget(url: RelativeUrl?): AbsoluteUrl? {
        return url?.let { AbsoluteUrl(urlsProvider.widgetsSite.value + it.value) }
    }

    fun makeSite(url: RelativeUrl?): AbsoluteUrl? {
        return url?.let { AbsoluteUrl(urlsProvider.site.value + it.value) }
    }

    fun makeBase(url: RelativeUrl?): AbsoluteUrl? {
        return url?.let { AbsoluteUrl(urlsProvider.base.value + it.value) }
    }

    fun makeMedia(url: RelativeUrl?): AbsoluteUrl? {
        return url?.let { AbsoluteUrl(urlsProvider.baseImages.value + it.value) }
    }
}
