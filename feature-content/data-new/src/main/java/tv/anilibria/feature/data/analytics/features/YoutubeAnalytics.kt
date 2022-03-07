package tv.anilibria.feature.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.feature.data.analytics.AnalyticsConstants
import tv.anilibria.feature.data.analytics.features.extensions.toIdParam
import tv.anilibria.feature.data.analytics.features.extensions.toNavFromParam
import tv.anilibria.feature.data.analytics.features.extensions.toVidParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class YoutubeAnalytics(
    private val sender: AnalyticsSender
) {

    fun openVideo(from: String, id: Long, vid: String?) {
        sender.send(
            AnalyticsConstants.youtube_video_open,
            from.toNavFromParam(),
            id.toIdParam(),
            vid.toVidParam()
        )
    }

}