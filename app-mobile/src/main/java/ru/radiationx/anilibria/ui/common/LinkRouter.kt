package ru.radiationx.anilibria.ui.common

import ru.radiationx.anilibria.navigation.BaseAppScreen
import ru.radiationx.anilibria.navigation.Screens
import ru.radiationx.anilibria.presentation.common.ILinkHandler
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.feature.analytics.api.AnalyticsConstants
import tv.anilibria.feature.analytics.api.features.ReleaseAnalytics
import tv.anilibria.feature.content.types.release.ReleaseCode
import java.util.regex.Pattern

@InjectConstructor
class LinkRouter(
    private val releaseAnalytics: ReleaseAnalytics
) : ILinkHandler {

    private val releaseDetail by lazy {
        Pattern.compile("\\/release\\/([\\s\\S]*?)\\.html|tracker\\/\\?ELEMENT_CODE=([^&]+)")
    }

    override fun handle(url: String, router: Router?, doNavigate: Boolean): Boolean {
        findScreen(url)?.also { screen ->
            if (doNavigate) {
                sendNavigateAnalytics(screen)
                router?.navigateTo(screen)
            }
            return true
        }
        return false
    }

    override fun findScreen(url: String): BaseAppScreen? {
        if (checkUnsupported(url)) {
            return null
        }
        releaseDetail.matcher(url).let {
            if (it.find()) {
                val code = it.group(1) ?: it.group(2)
                return Screens.ReleaseDetails(code = ReleaseCode(code))
            }
        }
        return null
    }

    private fun sendNavigateAnalytics(screen: BaseAppScreen) {
        when (screen) {
            is Screens.ReleaseDetails -> {
                releaseAnalytics.open(AnalyticsConstants.link_router, null, screen.code?.code)
            }
        }
    }

    private fun checkUnsupported(url: String): Boolean {
        if (url.contains("communication/forum")) return true
        return false
    }

}