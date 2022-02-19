package ru.radiationx.anilibria.screen.player.end_season

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.LifecycleViewModel
import ru.radiationx.anilibria.screen.player.PlayerController
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.module.data.ReleaseInteractor
import tv.anilibria.module.data.repos.EpisodeHistoryRepository
import tv.anilibria.module.domain.entity.release.Episode
import tv.anilibria.module.domain.entity.release.EpisodeId

@InjectConstructor
class EndSeasonViewModel(
    private val releaseInteractor: ReleaseInteractor,
    private val episodeHistoryRepository: EpisodeHistoryRepository,
    private val guidedRouter: GuidedRouter,
    private val playerController: PlayerController,
    private val router: Router
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

    fun onReplayEpisodeClick() {
        val episode = currentEpisode ?: return

        viewModelScope.launch {
            episodeHistoryRepository.saveSeek(episode.id, 0)
            playerController.selectEpisodeRelay.accept(episode.id)
            guidedRouter.close()
        }
    }

    fun onReplaySeasonClick() {
        currentEpisodes.firstOrNull()?.also { firstEpisode ->
            playerController.selectEpisodeRelay.accept(firstEpisode.id)
        }
        guidedRouter.close()
    }

    fun onCloseClick() {
        guidedRouter.finishGuidedChain()
        router.exit()
    }

}