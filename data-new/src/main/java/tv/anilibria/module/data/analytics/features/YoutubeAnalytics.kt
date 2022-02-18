package tv.anilibria.module.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.module.data.analytics.AnalyticsConstants
import tv.anilibria.module.data.analytics.features.extensions.toIdParam
import tv.anilibria.module.data.analytics.features.extensions.toNavFromParam
import tv.anilibria.module.data.analytics.features.extensions.toVidParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class YoutubeAnalytics(
    private val sender: AnalyticsSender
) {

    fun openVideo(from: String, id: Int, vid: String?) {
        sender.send(
            AnalyticsConstants.youtube_video_open,
            from.toNavFromParam(),
            id.toIdParam(),
            vid.toVidParam()
        )
    }

}