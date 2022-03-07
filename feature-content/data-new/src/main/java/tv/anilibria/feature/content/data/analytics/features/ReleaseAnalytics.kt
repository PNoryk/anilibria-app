package tv.anilibria.feature.content.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.analytics.AnalyticsConstants
import tv.anilibria.feature.content.data.analytics.features.extensions.toIdParam
import tv.anilibria.feature.content.data.analytics.features.extensions.toNavFromParam
import tv.anilibria.feature.content.data.analytics.features.extensions.toParam
import tv.anilibria.feature.content.data.analytics.features.extensions.toQualityParam
import tv.anilibria.feature.content.data.analytics.features.model.AnalyticsQuality
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class ReleaseAnalytics(
    private val sender: AnalyticsSender
) {

    private companion object {
        const val PARAM_HEVC = "hevc"
        const val PARAM_RELEASE_CODE = "code"
        const val PARAM_EXTERNAL_TAG = "tag"
    }

    fun open(from: String, releaseId: Long?, releaseCode: String? = null) {
        sender.send(
            AnalyticsConstants.release_open,
            from.toNavFromParam(),
            releaseId.toIdParam(),
            releaseCode.toParam(PARAM_RELEASE_CODE)
        )
    }

    fun copyLink(from: String, releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_copy,
            from.toNavFromParam(),
            releaseId.toIdParam()
        )
    }

    fun share(from: String, releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_share,
            from.toNavFromParam(),
            releaseId.toIdParam()
        )
    }

    fun shortcut(from: String, releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_shortcut,
            from.toNavFromParam(),
            releaseId.toIdParam()
        )
    }

    fun historyReset() {
        sender.send(AnalyticsConstants.release_history_reset)
    }

    fun historyViewAll() {
        sender.send(AnalyticsConstants.release_history_view_all)
    }

    fun historyResetEpisode() {
        sender.send(AnalyticsConstants.release_history_reset_episode)
    }

    fun episodesTopStartClick(releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_episodes_top_start,
            releaseId.toIdParam()
        )
    }

    fun episodesTopContinueClick(releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_episodes_top_continue,
            releaseId.toIdParam()
        )
    }

    fun episodesStartClick(releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_episodes_start,
            releaseId.toIdParam()
        )
    }

    fun episodesContinueClick(releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_episodes_continue,
            releaseId.toIdParam()
        )
    }

    fun episodePlayClick(quality: AnalyticsQuality, releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_episode_play,
            quality.toQualityParam(),
            releaseId.toIdParam()
        )
    }

    fun episodeExternalClick(releaseId: Long, externalTag: String) {
        sender.send(
            AnalyticsConstants.release_episode_external,
            releaseId.toIdParam(),
            externalTag.toParam(PARAM_EXTERNAL_TAG)
        )
    }

    fun episodeDownloadClick(quality: AnalyticsQuality, releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_episode_download,
            quality.toQualityParam(),
            releaseId.toIdParam()
        )
    }

    fun episodeDownloadByUrl(releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_episode_download_url,
            releaseId.toIdParam()
        )
    }

    fun webPlayerClick(releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_web_player,
            releaseId.toIdParam()
        )
    }

    fun torrentClick(isHevc: Boolean, releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_torrent,
            isHevc.toParam(PARAM_HEVC),
            releaseId.toIdParam()
        )
    }

    fun donateClick(releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_donate,
            releaseId.toIdParam()
        )
    }

    fun descriptionExpand(releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_description_expand,
            releaseId.toIdParam()
        )
    }

    fun descriptionLinkClick(releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_description_link,
            releaseId.toIdParam()
        )
    }

    fun scheduleClick(releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_schedule_click,
            releaseId.toIdParam()
        )
    }

    fun genreClick(releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_genre_click,
            releaseId.toIdParam()
        )
    }

    fun favoriteAdd(releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_favorite_add,
            releaseId.toIdParam()
        )
    }

    fun favoriteRemove(releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_favorite_remove,
            releaseId.toIdParam()
        )
    }

    fun commentsClick(releaseId: Long) {
        sender.send(
            AnalyticsConstants.release_comments_click,
            releaseId.toIdParam()
        )
    }

    fun episodesTabClick(releaseId: Long, tabTag: String) {
        sender.send(
            AnalyticsConstants.release_episodes_tab_click,
            releaseId.toIdParam(),
            tabTag.toParam(PARAM_EXTERNAL_TAG)
        )
    }
}