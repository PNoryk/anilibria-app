package tv.anilibria.feature.content.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.analytics.AnalyticsConstants
import tv.anilibria.feature.content.data.analytics.features.extensions.toNavFromParam
import tv.anilibria.feature.content.data.analytics.features.extensions.toParam
import tv.anilibria.feature.content.data.analytics.features.extensions.toTimeParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class UpdaterAnalytics(
    private val sender: AnalyticsSender
) {

    private companion object {
        const val PARAM_SOURCE_TITLE = "title"
    }

    fun open(from: String) {
        sender.send(
            AnalyticsConstants.updater_open,
            from.toNavFromParam()
        )
    }

    fun downloadClick() {
        sender.send(AnalyticsConstants.updater_download_click)
    }

    fun sourceDownload(sourceTitle: String) {
        sender.send(
            AnalyticsConstants.updater_source_download,
            sourceTitle.toParam(PARAM_SOURCE_TITLE)
        )
    }

    fun useTime(timeInMillis: Long) {
        sender.send(
            AnalyticsConstants.updater_use_time,
            timeInMillis.toTimeParam()
        )
    }

    fun appUpdateCardClick() {
        sender.send(AnalyticsConstants.app_update_card_click)
    }

    fun appUpdateCardCloseClick() {
        sender.send(AnalyticsConstants.app_update_card_close)
    }

}