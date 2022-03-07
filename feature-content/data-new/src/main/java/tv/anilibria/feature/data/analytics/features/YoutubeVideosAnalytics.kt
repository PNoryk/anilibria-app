package tv.anilibria.feature.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.feature.data.analytics.AnalyticsConstants
import tv.anilibria.feature.data.analytics.features.extensions.toNavFromParam
import tv.anilibria.feature.data.analytics.features.extensions.toPageParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class YoutubeVideosAnalytics(
    private val sender: AnalyticsSender
) {

    fun open(from: String) {
        sender.send(
            AnalyticsConstants.youtube_videos_open,
            from.toNavFromParam()
        )
    }

    fun videoClick() {
        sender.send(AnalyticsConstants.youtube_videos_video_click)
    }

    fun loadPage(page: Int) {
        sender.send(
            AnalyticsConstants.youtube_videos_load_page,
            page.toPageParam()
        )
    }

}