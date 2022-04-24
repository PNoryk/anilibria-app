package ru.radiationx.anilibria.screen.player.episodes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.LifecycleViewModel
import ru.radiationx.anilibria.screen.player.PlayerController
import ru.radiationx.shared.ktx.asTimeSecString
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.ReleaseInteractor
import tv.anilibria.feature.content.types.release.Episode
import tv.anilibria.feature.content.types.release.EpisodeId
import tv.anilibria.feature.player.data.EpisodeHistoryRepository
import java.util.*

@InjectConstructor
class PlayerEpisodesViewModel(
    private val releaseInteractor: ReleaseInteractor,
    private val episodeHistoryRepository: EpisodeHistoryRepository,
    private val guidedRouter: GuidedRouter,
    private val playerController: PlayerController
) : LifecycleViewModel() {

    lateinit var argEpisodeId: EpisodeId

    val episodesData = MutableLiveData<List<Pair<String, String?>>>()
    val selectedIndex = MutableLiveData<Int>()

    private val currentEpisodes = mutableListOf<Episode>()

    override fun onCreate() {
        super.onCreate()

        viewModelScope.launch {
            releaseInteractor.awaitRelease(argEpisodeId.releaseId, null).also {
                currentEpisodes.clear()
                currentEpisodes.addAll(it.episodes?.reversed().orEmpty())
            }
            val visits = episodeHistoryRepository.getByRelease(argEpisodeId.releaseId)
            episodesData.value = currentEpisodes.map { episode ->
                val visit = visits.find { it.id == episode.id }
                val description = if (visit?.isViewed == true && visit.playerSeek ?: 0 > 0) {
                    "Остановлена на ${Date(visit.playerSeek ?: 0).asTimeSecString()}"
                } else {
                    null
                }
                Pair(episode.title.orEmpty(), description)
            }
            selectedIndex.value = currentEpisodes.indexOfLast { it.id == argEpisodeId }
        }
    }

    fun applyEpisode(index: Int) {
        viewModelScope.launch {
            playerController.selectEpisodeRelay.emit(currentEpisodes[index].id)
            guidedRouter.close()
        }
    }
}