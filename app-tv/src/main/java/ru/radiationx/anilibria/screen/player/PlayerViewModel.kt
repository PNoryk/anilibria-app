package ru.radiationx.anilibria.screen.player

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.*
import ru.radiationx.data.datasource.holders.PreferencesHolder
import toothpick.InjectConstructor
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.module.data.ReleaseInteractor
import tv.anilibria.module.data.repos.EpisodeHistoryRepository
import tv.anilibria.module.domain.entity.EpisodeVisit
import tv.anilibria.module.domain.entity.release.Episode
import tv.anilibria.module.domain.entity.release.EpisodeId
import tv.anilibria.module.domain.entity.release.Release

@InjectConstructor
class PlayerViewModel(
    private val releaseInteractor: ReleaseInteractor,
    private val episodeHistoryRepository: EpisodeHistoryRepository,
    private val guidedRouter: GuidedRouter,
    private val playerController: PlayerController
) : LifecycleViewModel() {

    lateinit var argEpisodeId: EpisodeId

    val videoData = MutableLiveData<Video>()
    val qualityState = MutableLiveData<Int>()
    val speedState = MutableLiveData<Float>()
    val playAction = MutableLiveData<Boolean>()

    private var playerInfo: PlayerInfo? = null

    private var currentEpisode: Episode? = null
    private var currentQuality: Int? = null
    private var currentComplete: Boolean? = null

    override fun onCreate() {
        super.onCreate()

        qualityState.value = releaseInteractor.getQuality()
        speedState.value = releaseInteractor.getPlaySpeed()

        playerController
            .selectEpisodeRelay
            .observeOn(AndroidSchedulers.mainThread())
            .lifeSubscribe { episodeId ->
                playerInfo
                    ?.episodes
                    ?.firstOrNull { it.id == episodeId }
                    ?.also { playEpisode(it, true) }
            }

        releaseInteractor
            .observeQuality()
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .lifeSubscribe {
                currentQuality = handleRawQuality(it)
                updateQuality()
                updateEpisode()
            }

        releaseInteractor
            .observePlaySpeed()
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .lifeSubscribe {
                speedState.value = it
            }

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
            Video(url?.value!!, seek, info.release.titleRus?.text.orEmpty(), it.title.orEmpty())
        }
        if (force || videoData.value?.url != newVideo.url) {
            videoData.value = newVideo
        }
    }

    private fun handleRawQuality(quality: Int): Int = when (quality) {
        PreferencesHolder.QUALITY_NO,
        PreferencesHolder.QUALITY_ALWAYS -> PreferencesHolder.QUALITY_SD
        else -> quality
    }

    private fun getEpisodeQuality(episode: Episode, quality: Int): Int {
        var newQuality = quality

        if (newQuality == PreferencesHolder.QUALITY_FULL_HD && episode.urlFullHd == null) {
            newQuality = PreferencesHolder.QUALITY_HD
        }
        if (newQuality == PreferencesHolder.QUALITY_HD && episode.urlHd == null) {
            newQuality = PreferencesHolder.QUALITY_SD
        }
        if (newQuality == PreferencesHolder.QUALITY_SD && episode.urlSd == null) {
            newQuality = -1
        }

        return newQuality
    }

    private fun getEpisodeUrl(episode: Episode, quality: Int): AbsoluteUrl? =
        when (getEpisodeQuality(episode, quality)) {
            PreferencesHolder.QUALITY_FULL_HD -> episode.urlFullHd
            PreferencesHolder.QUALITY_HD -> episode.urlHd
            PreferencesHolder.QUALITY_SD -> episode.urlSd
            else -> null
        }

    private data class PlayerInfo(
        val release: Release,
        val episodes: List<Episode>,
        val episodeVisits: List<EpisodeVisit>
    )
}