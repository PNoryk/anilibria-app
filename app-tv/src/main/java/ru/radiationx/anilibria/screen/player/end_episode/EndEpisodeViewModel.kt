package ru.radiationx.anilibria.screen.player.end_episode

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.LifecycleViewModel
import ru.radiationx.anilibria.screen.player.PlayerController
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.ReleaseInteractor
import tv.anilibria.feature.content.types.release.Episode
import tv.anilibria.feature.content.types.release.EpisodeId
import tv.anilibria.feature.player.data.EpisodeHistoryRepository

@InjectConstructor
class EndEpisodeViewModel(
    private val releaseInteractor: ReleaseInteractor,
    private val episodeHistoryRepository: EpisodeHistoryRepository,
    private val guidedRouter: GuidedRouter,
    private val playerController: PlayerController
) : LifecycleViewModel() {

    lateinit var argEpisodeId: EpisodeId

    private val currentEpisodes = mutableListOf<Episode>()
    private val currentEpisode
        get() = currentEpisodes.firstOrNull { it.id == argEpisodeId }

    override fun onCreate() {
        super.onCreate()
        releaseInteractor
            .observeRelease(argEpisodeId.releaseId, null)
            .filterNotNull()
            .onEach {
                currentEpisodes.clear()
                currentEpisodes.addAll(it.episodes?.reversed().orEmpty())
            }
            .launchIn(viewModelScope)
    }

    fun onReplayClick() {
        val episode = currentEpisode ?: return

        viewModelScope.launch {
            episodeHistoryRepository.saveSeek(episode.id, 0)
            playerController.selectEpisodeRelay.emit(episode.id)
            guidedRouter.close()
        }
    }

    fun onNextClick() {
        val episode = currentEpisode ?: return

        viewModelScope.launch {
            val currentIndex = currentEpisodes.indexOfFirst { it.id == episode.id }

            currentEpisodes.getOrNull(currentIndex + 1)?.also { nextEpisode ->
                playerController.selectEpisodeRelay.emit(nextEpisode.id)
            }
            guidedRouter.close()
        }
    }
}