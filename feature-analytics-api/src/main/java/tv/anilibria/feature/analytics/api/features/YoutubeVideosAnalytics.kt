package tv.anilibria.feature.analytics.api.features

import toothpick.InjectConstructor
import tv.anilibria.feature.analytics.api.AnalyticsConstants
import tv.anilibria.feature.analytics.api.features.extensions.toNavFromParam
import tv.anilibria.feature.analytics.api.features.extensions.toPageParam
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