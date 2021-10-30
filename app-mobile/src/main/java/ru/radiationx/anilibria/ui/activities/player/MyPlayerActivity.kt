package ru.radiationx.anilibria.ui.activities.player

import android.app.ActivityManager
import android.content.*
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import com.devbrackets.android.exomedia.core.video.scale.ScaleType
import com.devbrackets.android.exomedia.listener.*
import com.devbrackets.android.exomedia.ui.widget.VideoControlsCore
import kotlinx.android.synthetic.main.activity_myplayer.*
import kotlinx.android.synthetic.main.view_video_control.*
import org.michaelbel.bottomsheet.BottomSheet
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.extension.getColorFromAttr
import ru.radiationx.anilibria.extension.isDark
import ru.radiationx.anilibria.ui.activities.BaseActivity
import ru.radiationx.anilibria.ui.widgets.VideoControlsAlib
import ru.radiationx.data.analytics.AnalyticsErrorReporter
import ru.radiationx.data.analytics.features.PlayerAnalytics
import ru.radiationx.data.analytics.features.mapper.toAnalyticsQuality
import ru.radiationx.data.analytics.features.model.AnalyticsEpisodeFinishAction
import ru.radiationx.data.analytics.features.model.AnalyticsSeasonFinishAction
import ru.radiationx.data.datasource.holders.AppThemeHolder
import ru.radiationx.data.datasource.holders.PreferencesHolder
import ru.radiationx.data.entity.app.release.ReleaseFull
import ru.radiationx.data.interactors.ReleaseInteractor
import ru.radiationx.shared.ktx.android.gone
import ru.radiationx.shared.ktx.android.visible
import ru.radiationx.shared_app.analytics.LifecycleTimeCounter
import ru.radiationx.shared_app.di.injectDependencies
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

fun PlayerQuality.toPrefQuality() = when (this) {
    PlayerQuality.SD -> PreferencesHolder.QUALITY_SD
    PlayerQuality.HD -> PreferencesHolder.QUALITY_HD
    PlayerQuality.FULL_HD -> PreferencesHolder.QUALITY_FULL_HD
}

class MyPlayerActivity : BaseActivity() {

    companion object {
        private const val ARG_RELEASE = "release"
        private const val ARG_EPISODE_ID = "episode_id"
        private const val ARG_QUALITY = "quality"
        private const val ARG_PLAY_FLAG = "play_flag"

        private const val DEFAULT_EPISODE_ID = -1
        private val DEFAULT_QUALITY = PlayerQuality.SD
        private val DEFAULT_PLAY_FLAG = PlayerPlayFlag.ASK
        private const val DEFAULT_PLAY_SPEED = 1.0f

        fun newIntent(
            context: Context,
            release: ReleaseFull,
            episodeId: Int,
            quality: PlayerQuality,
            playFlag: PlayerPlayFlag?
        ): Intent = Intent(context, MyPlayerActivity::class.java).apply {
            putExtra(ARG_RELEASE, release)
            putExtra(ARG_EPISODE_ID, episodeId)
            putExtra(ARG_QUALITY, quality)
            playFlag?.let {
                putExtra(ARG_PLAY_FLAG, it)
            }
        }
    }

    private lateinit var releaseData: ReleaseFull
    private var playFlag: PlayerPlayFlag = DEFAULT_PLAY_FLAG
    private var currentEpisodeId = DEFAULT_EPISODE_ID
    private var currentQuality = DEFAULT_QUALITY
    private var currentPlaySpeed = DEFAULT_PLAY_SPEED

    private var videoControls: VideoControlsAlib? = null

    @Inject
    lateinit var releaseInteractor: ReleaseInteractor

    @Inject
    lateinit var appThemeHolder: AppThemeHolder

    @Inject
    lateinit var defaultPreferences: SharedPreferences

    @Inject
    lateinit var appPreferences: PreferencesHolder

    @Inject
    lateinit var playerAnalytics: PlayerAnalytics

    @Inject
    lateinit var errorReporter: AnalyticsErrorReporter

    private val useTimeCounter by lazy {
        LifecycleTimeCounter(playerAnalytics::useTime)
    }

    private val defaultScale = ScaleType.FIT_CENTER
    private var currentScale = defaultScale

    private var currentPipControl = PreferencesHolder.PIP_BUTTON

    private var pipController: PlayerPipControllerImpl? = null
    private var systemUiController: PlayerSystemUiController? = null
    private var fullscreenController: PlayerFullscreenController? = null
    private var playerStatistics: PlayerStatistics? = null

    private val settingsDialogController by lazy {
        SettingDialogController(
            playerAnalytics = playerAnalytics,
            appThemeHolder = appThemeHolder,
            qualityListener = { updateQuality(it) },
            speedListener = { updatePlaySpeed(it) },
            scaleListener = { updateScale(it) },
            pipListener = { updatePipControl(it) }
        )
    }

    private val finishDialogController by lazy {
        FinishDialogController(appThemeHolder)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        currentPlaySpeed = loadPlaySpeed()
        currentPipControl = loadPIPControl()
        lifecycle.addObserver(useTimeCounter)
        initPipController()
        initSystemUiController()
        initFullScreenController()
        initPlayerStatistics()
        systemUiController?.onCreate()
        playerStatistics?.onCreate()
        setContentView(R.layout.activity_myplayer)

        player.setScaleType(currentScale)
        player.playbackSpeed = currentPlaySpeed
        player.setOnPreparedListener(playerListener)
        player.setOnCompletionListener(playerListener)
        player.setOnVideoSizedChangedListener { _, _, _ ->
            updatePipRect()
        }
        playerStatistics?.also {
            player.setAnalyticsListener(it.analyticsListener)
        }

        videoControls = VideoControlsAlib(ContextThemeWrapper(this, this.theme), null, 0)

        videoControls?.apply {
            setAnalytics(playerAnalytics)
            updatePipControl()
            player.setControls(this as VideoControlsCore)
            setOpeningListener(alibControlListener)
            setVisibilityListener(ControlsVisibilityListener())
            let {
                it.setNextButtonRemoved(false)
                it.setPreviousButtonRemoved(false)
                it.setButtonListener(controlsListener)
            }
        }
        handleIntent(intent)
        updateUiConfig()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.also { handleIntent(it) }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        pipController?.onModeChanged(isInPictureInPictureMode)
        Log.d("lalka", "onPictureInPictureModeChanged $isInPictureInPictureMode")
        saveEpisode()
        if (isInPictureInPictureMode) {
            videoControls?.hide()
            videoControls?.gone()
            playerAnalytics.pip(getSeekPercent())
        } else {
            player.showControls()
            videoControls?.visible()
        }
        updateUiConfig()
    }

    private fun initPipController() {
        if (checkPipFeature()) {
            pipController = PlayerPipControllerImpl(
                activity = this,
                playPauseListener = { controlsListener.onPlayPauseClicked() },
                prevListener = { controlsListener.onNextClicked() },
                nextListener = { controlsListener.onPreviousClicked() }
            )
        }
    }

    private fun initSystemUiController() {
        systemUiController = PlayerSystemUiController(this) {
            if (it) {
                player.showControls()
            }
        }
    }

    private fun initFullScreenController() {
        fullscreenController = PlayerFullscreenController(this) {
            if (it) {
                playerAnalytics.fullScreen(getSeekPercent())
            }
            videoControls?.setFullScreenMode(it)
        }
    }

    private fun initPlayerStatistics() {
        playerStatistics = PlayerStatistics(playerAnalytics, errorReporter) {
            currentQuality
        }
    }

    private fun checkSausage(): Boolean {
        val size = windowManager.defaultDisplay.let {
            val size = Point()
            it.getRealSize(size)
            size
        }
        val notSausage = 16f / 9f

        val width = max(size.x, size.y)
        val height = min(size.x, size.y)
        val ratio = width.toFloat() / height.toFloat()

        Log.e(
            "lululu",
            "checkSausage $width, $height, $ratio && $notSausage = ${ratio != notSausage}"
        )
        return notSausage != ratio
    }

    private fun handleIntent(intent: Intent) {
        val release = intent.getSerializableExtra(ARG_RELEASE) as ReleaseFull? ?: return
        val episodeId =
            intent.getIntExtra(
                ARG_EPISODE_ID,
                if (release.episodes.size > 0) 0 else DEFAULT_EPISODE_ID
            )
        val quality =
            (intent.getSerializableExtra(ARG_QUALITY) as PlayerQuality?) ?: DEFAULT_QUALITY
        val playFlag = (intent.getSerializableExtra(ARG_PLAY_FLAG) as PlayerPlayFlag?)
            ?: PlayerPlayFlag.ASK

        this.releaseData = release
        this.currentEpisodeId = episodeId
        this.currentQuality = quality
        this.playFlag = playFlag

        updateAndPlayRelease()
    }

    private fun updateAndPlayRelease() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTaskDescription(ActivityManager.TaskDescription(releaseData.title))
        }

        videoControls?.apply {
            setTitle(releaseData.title)
        }
        playEpisode(getEpisode())
    }

    private fun loadScale(orientation: Int): ScaleType {
        val scaleOrdinal =
            defaultPreferences.getInt("video_ratio_$orientation", defaultScale.ordinal)
        return ScaleType.fromOrdinal(scaleOrdinal)
    }

    private fun saveScale(orientation: Int, scale: ScaleType) {
        defaultPreferences.edit().putInt("video_ratio_$orientation", scale.ordinal).apply()
    }

    private fun savePlaySpeed() {
        playerAnalytics.settingsSpeedChange(currentPlaySpeed)
        releaseInteractor.setPlaySpeed(currentPlaySpeed)
    }

    private fun loadPlaySpeed(): Float {
        return releaseInteractor.getPlaySpeed()
    }

    private fun savePIPControl() {
        releaseInteractor.setPIPControl(currentPipControl)
    }

    private fun loadPIPControl(): Int {
        return releaseInteractor.getPIPControl()
    }

    private fun updateScale(scale: ScaleType) {
        val inMultiWindow = getInMultiWindow()
        currentScale = scale
        if (!inMultiWindow) {
            saveScale(resources.configuration.orientation, currentScale)
        }
        player?.setScaleType(currentScale)
    }

    private fun updateQuality(newQuality: PlayerQuality) {
        this.currentQuality = newQuality
        val prefQuality = newQuality.toPrefQuality()
        playerAnalytics.settingsQualityChange(prefQuality.toAnalyticsQuality())
        appPreferences.setQuality(prefQuality)
        saveEpisode()
        updateAndPlayRelease()
    }

    private fun updatePlaySpeed(newPlaySpeed: Float) {
        currentPlaySpeed = newPlaySpeed
        player.playbackSpeed = currentPlaySpeed
        savePlaySpeed()
    }

    private fun updatePipControl(newPipControl: Int = currentPipControl) {
        currentPipControl = newPipControl
        val pipCheck = checkPipFeature() && newPipControl == PreferencesHolder.PIP_BUTTON
        videoControls?.setPictureInPictureEnabled(pipCheck)
        savePIPControl()
    }

    private fun checkPipFeature(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
        }
        return false
    }

    override fun onUserLeaveHint() {
        if (checkPipFeature() && currentPipControl == PreferencesHolder.PIP_AUTO) {
            pipController?.enterPipMode()
        }
    }

    override fun onStop() {
        super.onStop()
        player.pause()
    }

    private fun getInMultiWindow(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            isInMultiWindowMode
        } else {
            false
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateUiConfig()
    }

    private fun updateUiConfig() {
        fullscreenController?.updateConfig()
        systemUiController?.updateConfig()
        updatePipRect()

        val currentOrientation = resources.configuration.orientation
        val scale = loadScale(currentOrientation)
        val inMultiWindow = getInMultiWindow()

        updateScale(if (inMultiWindow) defaultScale else scale)

        videoControls?.fitSystemWindows(inMultiWindow || currentOrientation != Configuration.ORIENTATION_LANDSCAPE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = if (inMultiWindow) {
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
            } else {
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
            window.attributes = window.attributes
        }
    }

    private fun updatePipRect() {
        player
            ?.findViewById<View>(com.devbrackets.android.exomedia.R.id.exomedia_video_view)
            ?.also {
                val rect = Rect()
                it.getGlobalVisibleRect(rect)
                pipController?.updateRect(rect)
            }
    }

    private fun saveEpisode(position: Long = player.currentPosition) {
        if (position < 0) {
            return
        }
        releaseInteractor.putEpisode(getEpisode().apply {
            Log.e("SUKA", "Set posistion seek: ${position}")
            seek = position
            lastAccess = System.currentTimeMillis()
            isViewed = true
        })
    }

    override fun onDestroy() {
        saveEpisode()
        player.stopPlayback()
        super.onDestroy()
        pipController?.onDestroy()
        systemUiController?.onDestroy()
        playerStatistics?.onDestroy()
        pipController = null
        systemUiController = null
        fullscreenController = null
        playerStatistics = null
    }

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

    private fun getEpisode(id: Int = currentEpisodeId) = releaseData.episodes.first { it.id == id }

    private fun getEpisodeId(episode: ReleaseFull.Episode) =
        releaseData.episodes.first { it == episode }.id

    private fun playEpisode(episode: ReleaseFull.Episode) {
        when (playFlag) {
            PlayerPlayFlag.ASK -> {
                hardPlayEpisode(episode)
                if (episode.seek > 0) {
                    hardPlayEpisode(episode)
                    val titles = arrayOf("К началу", "К последней позиции")
                    AlertDialog.Builder(this)
                        .setTitle("Перемотать")
                        .setItems(titles) { _, which ->
                            if (which == 1) {
                                player.seekTo(episode.seek)
                            }
                        }
                        .show()
                }
            }
            PlayerPlayFlag.START -> {
                hardPlayEpisode(episode)
            }
            PlayerPlayFlag.CONTINUE -> {
                hardPlayEpisode(episode)
                player.seekTo(episode.seek)
            }
        }
        playFlag = PlayerPlayFlag.CONTINUE
    }

    private fun hardPlayEpisode(episode: ReleaseFull.Episode) {
        toolbar.subtitle =
            "${episode.title} [${settingsDialogController.getQualityTitle(currentQuality)}]"
        currentEpisodeId = getEpisodeId(episode)
        val videoPath = when (currentQuality) {
            PlayerQuality.SD -> episode.urlSd
            PlayerQuality.HD -> episode.urlHd
            PlayerQuality.FULL_HD -> episode.urlFullHd
        }
        player.setVideoPath(videoPath)
    }

    private fun showSeasonFinishDialog() {
        playerAnalytics.seasonFinish()

        finishDialogController.showSeasonFinishDialog(
            context = this,
            episodeRestartListener = {
                playerAnalytics.seasonFinishAction(AnalyticsSeasonFinishAction.RESTART_EPISODE)
                saveEpisode(0)
                hardPlayEpisode(getEpisode())
            },
            seasonRestartListener = {
                playerAnalytics.seasonFinishAction(AnalyticsSeasonFinishAction.RESTART_SEASON)
                releaseData.episodes.lastOrNull()?.also {
                    hardPlayEpisode(it)
                }
            },
            closePlayerListener = {
                playerAnalytics.seasonFinishAction(AnalyticsSeasonFinishAction.CLOSE_PLAYER)
                finish()
            },
        )
    }

    private fun showEpisodeFinishDialog() {
        playerAnalytics.episodesFinish()
        finishDialogController.showEpisodeFinishDialog(
            context = this,
            episodeRestartListener = {
                playerAnalytics.episodesFinishAction(AnalyticsEpisodeFinishAction.RESTART)
                saveEpisode(0)
                hardPlayEpisode(getEpisode())
            },
            startNextListener = {
                playerAnalytics.episodesFinishAction(AnalyticsEpisodeFinishAction.NEXT)
                getNextEpisode()?.also { hardPlayEpisode(it) }
            }
        )
    }

    private fun getSeekPercent(): Float {
        if (player == null) return 0f
        if (player.duration <= 0) {
            return 0f
        }
        return player.currentPosition / player.duration.toFloat()
    }

    private val alibControlListener = object : VideoControlsAlib.AlibControlsListener {

        override fun onPIPClick() {
            pipController?.enterPipMode()
        }

        override fun onBackClick() {
            finish()
        }

        override fun onSettingsClick() {
            playerAnalytics.settingsClick()
            settingsDialogController.showSettingsDialog(
                context = this@MyPlayerActivity,
                episode = getEpisode(),
                currentQuality = currentQuality,
                currentPlaySpeed = currentPlaySpeed,
                currentScale = currentScale,
                currentPipControl = currentPipControl,
                currentIsSausage = checkSausage(),
                currentIsPipMode = checkPipFeature()
            )
        }

        private val delta = TimeUnit.SECONDS.toMillis(90)
        override fun onMinusClick() {
            val newPosition = player.currentPosition - delta
            player.seekTo(newPosition.coerceIn(0, player.duration))
        }

        override fun onPlusClick() {
            val newPosition = player.currentPosition + delta
            player.seekTo(newPosition.coerceIn(0, player.duration))
        }

        override fun onFullScreenClick() {
            fullscreenController?.toggle()
        }

        override fun onPlaybackStateChanged(isPlaying: Boolean) {
            pipController?.updatePlayingState(isPlaying)
        }
    }

    private val playerListener = object : OnPreparedListener, OnCompletionListener {
        override fun onPrepared() {
            val episode = getEpisode()
            if (episode.seek >= player.duration) {
                player.stopPlayback()
                if (getNextEpisode() == null) {
                    showSeasonFinishDialog()
                } else {
                    showEpisodeFinishDialog()
                }
            } else {
                player.start()
            }
        }

        override fun onCompletion() {
            if (!controlsListener.onNextClicked()) {
                showSeasonFinishDialog()
            }
        }
    }

    private val controlsListener = object : VideoControlsButtonListener {
        override fun onPlayPauseClicked(): Boolean {
            if (player.isPlaying) {
                playerAnalytics.pauseClick()
                player.pause()
            } else {
                playerAnalytics.playClick()
                player.start()
            }
            return true
        }

        override fun onNextClicked(): Boolean {
            playerAnalytics.nextClick(getSeekPercent())
            saveEpisode()
            val episode = getNextEpisode() ?: return false
            playEpisode(episode)
            return true
        }

        override fun onPreviousClicked(): Boolean {
            playerAnalytics.prevClick(getSeekPercent())
            saveEpisode()
            val episode = getPrevEpisode() ?: return false
            playEpisode(episode)
            return true
        }

        override fun onRewindClicked(): Boolean {
            return false
        }

        override fun onFastForwardClicked(): Boolean {
            return false
        }
    }

    private inner class ControlsVisibilityListener : VideoControlsVisibilityListener {
        override fun onControlsShown() {
            Log.e("MyPlayer", "onControlsShown $supportActionBar, ${supportActionBar?.isShowing}")
        }

        override fun onControlsHidden() {
            Log.e("MyPlayer", "onControlsHidden $supportActionBar")
            systemUiController?.goFullscreen()
        }
    }
}
