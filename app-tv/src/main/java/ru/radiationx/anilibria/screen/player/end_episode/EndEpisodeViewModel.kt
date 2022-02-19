package ru.radiationx.anilibria.screen.player.end_episode

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.LifecycleViewModel
import ru.radiationx.anilibria.screen.player.PlayerController
import toothpick.InjectConstructor
import tv.anilibria.module.data.ReleaseInteractor
import tv.anilibria.module.data.repos.EpisodeHistoryRepository
import tv.anilibria.module.domain.entity.release.Episode
import tv.anilibria.module.domain.entity.release.EpisodeId

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
        releaseInteractor.getFull(argEpisodeId.releaseId)?.also {
            currentEpisodes.clear()
            currentEpisodes.addAll(it.episodes?.reversed().orEmpty())
        }
    }

    fun onReplayClick() {
        val episode = currentEpisode ?: return

        viewModelScope.launch {
            episodeHistoryRepository.saveSeek(episode.id, 0)
            playerController.selectEpisodeRelay.accept(episode.id)
            guidedRouter.close()
        }
    }

    fun onNextClick() {
        val episode = currentEpisode ?: return
        val currentIndex = currentEpisodes.indexOfFirst { it.id == episode.id }

        currentEpisodes.getOrNull(currentIndex + 1)?.also { nextEpisode ->
            playerController.selectEpisodeRelay.accept(nextEpisode.id)
        }
        guidedRouter.close()
    }
}