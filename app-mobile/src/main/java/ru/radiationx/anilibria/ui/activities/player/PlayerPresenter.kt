package ru.radiationx.anilibria.ui.activities.player

import android.content.SharedPreferences
import android.util.Log
import com.devbrackets.android.exomedia.core.video.scale.ScaleType
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.ui.activities.player.controllers.PlaybackPosition
import ru.radiationx.anilibria.ui.activities.player.controllers.PlaybackState
import ru.radiationx.data.analytics.AnalyticsErrorReporter
import ru.radiationx.data.analytics.features.PlayerAnalytics
import ru.radiationx.data.analytics.features.mapper.toAnalyticsQuality
import ru.radiationx.data.analytics.features.model.AnalyticsEpisodeFinishAction
import ru.radiationx.data.analytics.features.model.AnalyticsSeasonFinishAction
import ru.radiationx.data.datasource.holders.AppThemeHolder
import ru.radiationx.data.datasource.holders.PreferencesHolder
import ru.radiationx.data.entity.app.release.ReleaseFull
import ru.radiationx.data.interactors.ReleaseInteractor
import ru.terrakok.cicerone.Router

class PlayerPresenter(
    private val router: Router,
    private val releaseInteractor: ReleaseInteractor,
    private val appThemeHolder: AppThemeHolder,
    private val defaultPreferences: SharedPreferences,
    private val appPreferences: PreferencesHolder,
    private val playerAnalytics: PlayerAnalytics,
    private val errorReporter: AnalyticsErrorReporter,
    private val playbackDialogController: PlaybackDialogController
) : BasePresenter<PlayerMvpView>(router) {

    companion object {

        private const val DEFAULT_EPISODE_ID = -1
        private val DEFAULT_QUALITY = PlayerQuality.SD
        private val DEFAULT_PLAY_FLAG = PlayerPlayFlag.ASK
        private const val DEFAULT_PLAY_SPEED = 1.0f
    }

    private lateinit var releaseData: ReleaseFull


    fun onPlayPauseClick(state: PlaybackState) {
        if (state.isPlaying) {
            playerAnalytics.pauseClick()
            viewState.pausePlayer()
        } else {
            playerAnalytics.playClick()
            viewState.playPlayer()
        }
    }

    fun onPrevClick(state: PlaybackState) {
        playerAnalytics.prevClick(state.getPercent())
        saveEpisode(state.position)
        val episode = getPrevEpisode() ?: return
        playEpisode(episode)
    }

    fun onNextClick(state: PlaybackState) {
        playerAnalytics.nextClick(state.getPercent())
        saveEpisode(state.position)
        val episode = getNextEpisode() ?: return
        playEpisode(episode)
    }

    fun onPrepared(state: PlaybackState) {
        val episode = getEpisode()
        if (episode.seek >= state.endPosition.position) {
            viewState.stopPlayer()
            if (getNextEpisode() == null) {
                showSeasonFinishDialog()
            } else {
                showEpisodeFinishDialog(state)
            }
        } else {
            viewState.playPlayer()
        }
    }

    fun onComplete() {
        if (getNextEpisode() == null) {
            showSeasonFinishDialog()
        }
    }


    /* OLD */

    private val defaultScale = ScaleType.FIT_CENTER
    private fun loadScale(orientation: Int): ScaleType {
        val scaleOrdinal =
            defaultPreferences.getInt("video_ratio_$orientation", defaultScale.ordinal)
        return ScaleType.fromOrdinal(scaleOrdinal)
    }

    fun saveScale(orientation: Int, scale: ScaleType) {
        defaultPreferences.edit().putInt("video_ratio_$orientation", scale.ordinal).apply()
    }

    fun savePlaySpeed(currentPlaySpeed: Float) {
        playerAnalytics.settingsSpeedChange(currentPlaySpeed)
        releaseInteractor.setPlaySpeed(currentPlaySpeed)
    }

    private fun loadPlaySpeed(): Float {
        return releaseInteractor.getPlaySpeed()
    }

    fun savePIPControl(currentPipControl: Int) {
        releaseInteractor.setPIPControl(currentPipControl)
    }

    private fun loadPIPControl(): Int {
        return releaseInteractor.getPIPControl()
    }

    private var currentEpisodeId = -1

    private fun getEpisode(id: Int = currentEpisodeId) = releaseData.episodes.first { it.id == id }

    private fun checkIndex(id: Int): Boolean {
        val lastId = releaseData.episodes.last().id
        val firstId = releaseData.episodes.first().id
        return id in lastId..firstId
    }

    private fun getNextEpisode(): ReleaseFull.Episode? {
        val nextId = currentEpisodeId + 1
        if (checkIndex(nextId)) {
            Log.e("S_DEF_LOG", "NEXT INDEX $nextId")
            return getEpisode(nextId)
        }
        return null
    }

    private fun getPrevEpisode(): ReleaseFull.Episode? {
        val prevId = currentEpisodeId - 1
        if (checkIndex(prevId)) {
            Log.e("S_DEF_LOG", "PREV INDEX $prevId")
            return getEpisode(prevId)
        }
        return null
    }


    fun saveEpisode(position: PlaybackPosition) {
        if (position.position < 0) {
            return
        }
        releaseInteractor.putEpisode(getEpisode().apply {
            Log.e("SUKA", "Set posistion seek: ${position}")
            seek = position.position
            lastAccess = System.currentTimeMillis()
            isViewed = true
        })
    }


    fun updateQuality(newQuality: PlayerQuality, state: PlaybackState) {
        val prefQuality = newQuality.toPrefQuality()
        playerAnalytics.settingsQualityChange(prefQuality.toAnalyticsQuality())
        appPreferences.setQuality(prefQuality)
        saveEpisode(state.position)
        updateAndPlayRelease()
    }

    fun onNewEpisode(
        releaseFull: ReleaseFull,
        episodeId: Int,
        quality: PlayerQuality,
        playFlag: PlayerPlayFlag
    ) {
        viewState.stopPlayer()
        releaseData = releaseFull
        currentEpisodeId = episodeId
        currentQuality = quality
        currentPlayFlag = playFlag
        updateAndPlayRelease()
    }


    private fun updateAndPlayRelease() {
        viewState.setReleaseInfo(releaseData.title.orEmpty())
        playEpisode(getEpisode())
    }

    private var currentPlayFlag: PlayerPlayFlag = DEFAULT_PLAY_FLAG
    private var currentQuality = DEFAULT_QUALITY

    private fun playEpisode(episode: ReleaseFull.Episode) {
        when (currentPlayFlag) {
            PlayerPlayFlag.ASK -> {
                if (episode.seek > 0) {
                    playbackDialogController.showAskStartEpisodeDialog(
                        onStartClick = {
                            hardPlayEpisode(episode, PlaybackPosition(0))
                        },
                        onContinueClick = {
                            hardPlayEpisode(episode, PlaybackPosition(episode.seek))
                        }
                    )
                } else {
                    hardPlayEpisode(episode, PlaybackPosition(0))
                }
            }
            PlayerPlayFlag.START -> {
                hardPlayEpisode(episode, PlaybackPosition(0))
            }
            PlayerPlayFlag.CONTINUE -> {
                hardPlayEpisode(episode, PlaybackPosition(episode.seek))
            }
        }
        currentPlayFlag = PlayerPlayFlag.CONTINUE
    }

    private fun getEpisodeId(episode: ReleaseFull.Episode) =
        releaseData.episodes.first { it == episode }.id

    private fun hardPlayEpisode(episode: ReleaseFull.Episode, position: PlaybackPosition) {
        currentEpisodeId = getEpisodeId(episode)
        val videoPath = when (currentQuality) {
            PlayerQuality.SD -> episode.urlSd
            PlayerQuality.HD -> episode.urlHd
            PlayerQuality.FULL_HD -> episode.urlFullHd
        }
        viewState.startPlayer(videoPath.orEmpty(), position)
        viewState.setEpisodeInfo(episode.title.orEmpty(), currentQuality)
    }


    private fun showSeasonFinishDialog() {
        playerAnalytics.seasonFinish()

        playbackDialogController.showSeasonFinishDialog(
            episodeRestartListener = {
                playerAnalytics.seasonFinishAction(AnalyticsSeasonFinishAction.RESTART_EPISODE)
                saveEpisode(PlaybackPosition(0))
                hardPlayEpisode(getEpisode(), PlaybackPosition(0))
            },
            seasonRestartListener = {
                playerAnalytics.seasonFinishAction(AnalyticsSeasonFinishAction.RESTART_SEASON)
                releaseData.episodes.lastOrNull()?.also {
                    hardPlayEpisode(it, PlaybackPosition(0))
                }
            },
            closePlayerListener = {
                playerAnalytics.seasonFinishAction(AnalyticsSeasonFinishAction.CLOSE_PLAYER)
                viewState.closeScreen()
            }
        )
    }

    private fun showEpisodeFinishDialog(state: PlaybackState) {
        playerAnalytics.episodesFinish()
        playbackDialogController.showEpisodeFinishDialog(
            episodeRestartListener = {
                playerAnalytics.episodesFinishAction(AnalyticsEpisodeFinishAction.RESTART)
                saveEpisode(PlaybackPosition(0))
                hardPlayEpisode(getEpisode(), PlaybackPosition(0))
            },
            startNextListener = {
                playerAnalytics.episodesFinishAction(AnalyticsEpisodeFinishAction.NEXT)
                onNextClick(state)
            }
        )
    }


}