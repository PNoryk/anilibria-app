package tv.anilibria.feature.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.feature.data.analytics.AnalyticsConstants
import tv.anilibria.feature.data.analytics.features.extensions.toErrorParam
import tv.anilibria.feature.data.analytics.features.extensions.toIdParam
import tv.anilibria.feature.data.analytics.features.extensions.toNavFromParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class CommentsAnalytics(
    private val sender: AnalyticsSender
) {

    fun open(from: String, releaseId: Long) {
        sender.send(
            AnalyticsConstants.comments_open,
            from.toNavFromParam(),
            releaseId.toIdParam()
        )
    }

    fun loaded() {
        sender.send(AnalyticsConstants.comments_loaded)
    }

    fun error(error: Throwable) {
        sender.send(
            AnalyticsConstants.comments_error,
            error.toErrorParam()
        )
    }

}