package ru.radiationx.anilibria.screen.player

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.*
import toothpick.InjectConstructor
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.feature.player.data.PlayerPreferencesStorage
import tv.anilibria.feature.player.data.prefs.PrefferedPlayerQuality
import tv.anilibria.feature.data.ReleaseInteractor
import tv.anilibria.feature.player.data.EpisodeHistoryRepository
import tv.anilibria.feature.player.data.domain.EpisodeVisit
import tv.anilibria.feature.domain.entity.release.Episode
import tv.anilibria.feature.domain.entity.release.EpisodeId
import tv.anilibria.feature.domain.entity.release.Release

@InjectConstructor
class PlayerViewModel(
    private val releaseInteractor: ReleaseInteractor,
    private val episodeHistoryRepository: EpisodeHistoryRepository,
    private val playerPreferencesStorage: PlayerPreferencesStorage,
    private val guidedRouter: GuidedRouter,
    private val playerController: PlayerController
) : LifecycleViewModel() {

    lateinit var argEpisodeId: EpisodeId

    val videoData = MutableLiveData<Video>()
    val qualityState = MutableLiveData<PrefferedPlayerQuality>()
    val speedState = MutableLiveData<Float>()
    val playAction = MutableLiveData<Boolean>()

    private var playerInfo: PlayerInfo? = null

    private var currentEpisode: Episode? = null
    private var currentQuality: PrefferedPlayerQuality? = null
    private var currentComplete: Boolean? = null

    override fun onCreate() {
        super.onCreate()

        viewModelScope.launch {
            qualityState.value = playerPreferencesStorage.quality.get()
            speedState.value = playerPreferencesStorage.playSpeed.get()
        }

        playerController
            .selectEpisodeRelay
            .onEach { episodeId ->
                playerInfo
                    ?.episodes
                    ?.firstOrNull { it.id == episodeId }
                    ?.also { playEpisode(it, true) }
            }
            .launchIn(viewModelScope)

        playerPreferencesStorage
            .quality.observe()
            .distinctUntilChanged()
            .onEach {
                currentQuality = handleRawQuality(it)
                updateQuality()
                updateEpisode()
            }
            .launchIn(viewModelScope)

        playerPreferencesStorage
            .playSpeed.observe()
            .distinctUntilChanged()
            .onEach {
                speedState.value = it
            }
            .launchIn(viewModelScope)

        releaseInteractor
            .observeFull(argEpisodeId.releaseId)
            .flatMapLatest { release ->
                episodeHistoryRepository
                    .observeByRelease(argEpisodeId.releaseId)
                    .map { visits ->
                        PlayerInfo(
                            release,
                            release.episodes?.reversed().orEmpty(),
                            visits
                        )
                    }
            }
            .onEach { playerInfo ->
                this.playerInfo = playerInfo
                val episodeId = currentEpisode?.id ?: argEpisodeId
                val episode = playerInfo.episodes
                    .find { it.id == episodeId }
                    ?: playerInfo.episodes.firstOrNull()
                episode?.also { playEpisode(it) }
            }
            .launchIn(viewModelScope)
    }

    fun onPlayClick(position: Long) {

    }

    fun onPauseClick(position: Long) {
        saveEpisode(position)
    }

    fun onReplayClick(position: Long) {

    }

    fun onNextClick(position: Long) {
        getNextEpisode()?.also {
            saveEpisode(position)
            playEpisode(it)
        }
    }

    fun onPrevClick(position: Long) {
        getPrevEpisode()?.also {
            saveEpisode(position)
            playEpisode(it)
        }
    }

    fun onEpisodesClick(position: Long) {
        val episode = currentEpisode ?: return
        saveEpisode(position)
        guidedRouter.open(PlayerEpisodesGuidedScreen(episode.id))
    }


    fun onQualityClick(position: Long) {
        val episode = currentEpisode ?: return
        saveEpisode(position)
        guidedRouter.open(PlayerQualityGuidedScreen(episode.id))
    }

    fun onSpeedClick(position: Long) {
        val episode = currentEpisode ?: return
        guidedRouter.open(PlayerSpeedGuidedScreen(episode.id))
    }

    fun onComplete(position: Long) {
        val episode = currentEpisode ?: return
        if (currentComplete == true) return
        currentComplete = true

        saveEpisode(position)
        val nextEpisode = getNextEpisode()
        if (nextEpisode != null) {
            playEpisode(nextEpisode)
        } else {
            guidedRouter.open(PlayerEndSeasonGuidedScreen(episode.id))
        }
        Log.e("kokoko", "onComplete $position")
    }

    fun onPrepare(position: Long, duration: Long) {
        val episode = currentEpisode ?: return
        val info = playerInfo ?: return
        val visit = info.episodeVisits.find { it.id == episode.id }
        val complete = visit?.playerSeek ?: 0 >= duration
        if (currentComplete == complete) return
        currentComplete = complete
        if (complete) {
            playAction.value = false
            val nextEpisode = getNextEpisode()
            if (nextEpisode == null) {
                guidedRouter.open(PlayerEndSeasonGuidedScreen(episode.id))
            } else {
                guidedRouter.open(PlayerEndEpisodeGuidedScreen(episode.id))
            }
        } else {
            playAction.value = true
        }
    }

    private fun getNextEpisode(): Episode? =
        playerInfo?.episodes?.getOrNull(getCurrentEpisodeIndex() + 1)

    private fun getPrevEpisode(): Episode? =
        playerInfo?.episodes?.getOrNull(getCurrentEpisodeIndex() - 1)

    private fun getCurrentEpisodeIndex(): Int =
        playerInfo?.episodes?.indexOfFirst { it.id == currentEpisode?.id } ?: -1

    private fun saveEpisode(position: Long) {
        val episode = currentEpisode ?: return
        if (position < 0) {
            return
        }
        viewModelScope.launch {
            episodeHistoryRepository.saveSeek(episode.id, position)
        }
    }

    private fun playEpisode(episode: Episode, force: Boolean = false) {
        currentEpisode = episode
        currentComplete = null
        updateQuality()
        updateEpisode(force)
    }

    private fun updateQuality() {
        val quality = currentQuality ?: return
        qualityState.value = currentEpisode?.let { getEpisodeQuality(it, quality) } ?: quality
    }

    private fun updateEpisode(force: Boolean = false) {
        val info = playerInfo ?: return
        val episode = currentEpisode ?: return
        val quality = currentQuality ?: return

        val newVideo = episode.let {
            val seek = info.episodeVisits.find { it.id == episode.id }?.playerSeek ?: 0
            val url = getEpisodeUrl(it, quality)
            Video(url?.value!!, seek, info.release.nameRus?.text.orEmpty(), it.title.orEmpty())
        }
        if (force || videoData.value?.url != newVideo.url) {
            videoData.value = newVideo
        }
    }

    private fun handleRawQuality(quality: PrefferedPlayerQuality): PrefferedPlayerQuality =
        when (quality) {
            PrefferedPlayerQuality.NOT_SELECTED,
            PrefferedPlayerQuality.ALWAYS_ASK -> PrefferedPlayerQuality.SD
            else -> quality
        }

    private fun getEpisodeQuality(
        episode: Episode,
        quality: PrefferedPlayerQuality
    ): PrefferedPlayerQuality? {
        var newQuality: PrefferedPlayerQuality? = quality

        if (newQuality == PrefferedPlayerQuality.FULL_HD && episode.urlFullHd == null) {
            newQuality = PrefferedPlayerQuality.HD
        }
        if (newQuality == PrefferedPlayerQuality.HD && episode.urlHd == null) {
            newQuality = PrefferedPlayerQuality.SD
        }
        if (newQuality == PrefferedPlayerQuality.SD && episode.urlSd == null) {
            newQuality = null
        }

        return newQuality
    }

    private fun getEpisodeUrl(episode: Episode, quality: PrefferedPlayerQuality): AbsoluteUrl? =
        when (getEpisodeQuality(episode, quality)) {
            PrefferedPlayerQuality.FULL_HD -> episode.urlFullHd
            PrefferedPlayerQuality.HD -> episode.urlHd
            PrefferedPlayerQuality.SD -> episode.urlSd
            else -> null
        }

    private data class PlayerInfo(
        val release: Release,
        val episodes: List<Episode>,
        val episodeVisits: List<EpisodeVisit>
    )
}