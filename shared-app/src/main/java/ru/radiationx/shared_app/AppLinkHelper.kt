package ru.radiationx.shared_app

import ru.radiationx.shared_app.common.SystemUtils
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.module.data.BaseUrlHelper

class AppLinkHelper(
    val urlHelper: BaseUrlHelper,
    val systemUtils: SystemUtils
) {


    fun shareLink(link: RelativeUrl?) {
        shareLink(urlHelper.makeSite(link))
    }

    fun shareLink(link: AbsoluteUrl?) {
        if (link != null) {
            systemUtils.shareText(link.value)
        }
    }

    fun copyLink(link: RelativeUrl?) {
        copyLink(urlHelper.makeSite(link))
    }

    fun copyLink(link: AbsoluteUrl?) {
        if (link != null) {
            systemUtils.copyToClipBoard(link.value)
        }
    }

    fun openLink(link: RelativeUrl?) {
        openLink(urlHelper.makeSite(link))
    }

    fun openLink(link: AbsoluteUrl?) {
        if (link != null) {
            systemUtils.externalLink(link.value)
        }
    }
}