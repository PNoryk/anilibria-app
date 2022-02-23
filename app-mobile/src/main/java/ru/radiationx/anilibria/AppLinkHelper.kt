package ru.radiationx.anilibria

import ru.radiationx.shared_app.common.SystemUtils
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.module.data.UrlHelper

class AppLinkHelper(
    val urlHelper: UrlHelper,
    val systemUtils: SystemUtils
) {


    fun shareLink(link: RelativeUrl?) {
        urlHelper.makeSite(link)?.also {
            systemUtils.shareText(it.value)
        }
    }

    fun shareLink(link: AbsoluteUrl?) {
        if (link != null) {
            systemUtils.shareText(link.value)
        }
    }

    fun copyLink(link: RelativeUrl?) {
        urlHelper.makeSite(link)?.also {
            systemUtils.copyToClipBoard(it.value)
        }
    }

    fun copyLink(link: AbsoluteUrl?) {
        if (link != null) {
            systemUtils.copyToClipBoard(link.value)
        }
    }

    fun open(link: RelativeUrl?) {
        urlHelper.makeSite(link)?.also {
            systemUtils.externalLink(it.value)
        }
    }

    fun open(link: AbsoluteUrl?) {
        if (link != null) {
            systemUtils.externalLink(link.value)
        }
    }
}