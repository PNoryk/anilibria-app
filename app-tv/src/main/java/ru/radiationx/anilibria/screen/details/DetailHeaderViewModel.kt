package ru.radiationx.anilibria.screen.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import ru.radiationx.anilibria.common.DetailDataConverter
import ru.radiationx.anilibria.common.DetailsState
import ru.radiationx.anilibria.common.LibriaDetails
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.AuthGuidedScreen
import ru.radiationx.anilibria.screen.LifecycleViewModel
import ru.radiationx.anilibria.screen.PlayerEpisodesGuidedScreen
import ru.radiationx.anilibria.screen.PlayerScreen
import ru.radiationx.anilibria.screen.player.PlayerController
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.feature.auth.data.AuthStateHolder
import tv.anilibria.feature.auth.data.domain.AuthState
import tv.anilibria.feature.data.ReleaseInteractor
import tv.anilibria.feature.player.data.EpisodeHistoryRepository
import tv.anilibria.feature.data.repos.FavoriteRepository
import tv.anilibria.feature.player.data.domain.EpisodeVisit
import tv.anilibria.feature.domain.entity.release.Release
import tv.anilibria.feature.domain.entity.release.ReleaseId

@InjectConstructor
class DetailHeaderViewModel(
    private val releaseInteractor: ReleaseInteractor,
    private val favoriteRepository: FavoriteRepository,
    private val episodeHistoryRepository: EpisodeHistoryRepository,
    private val authStateHolder: AuthStateHolder,
    private val converter: DetailDataConverter,
    private val router: Router,
    private val guidedRouter: GuidedRouter,
    private val playerController: PlayerController
) : LifecycleViewModel() {

    lateinit var releaseId: ReleaseId

    val releaseData = MutableLiveData<LibriaDetails>()
    val progressState = MutableLiveData<DetailsState>()

    private var currentRelease: Release? = null
    private var currentVisits = mutableListOf<EpisodeVisit>()

    private var selectEpisodeJob: Job? = null
    private var favoriteJob: Job? = null

    override fun onCreate() {
        super.onCreate()

        (releaseInteractor.getFull(releaseId) ?: releaseInteractor.getItem(releaseId))?.also {
            currentRelease = it
            update()
        }
        updateProgress()

        episodeHistoryRepository
            .observeByRelease(releaseId)
            .onEach {
                currentVisits.clear()
                currentVisits.addAll(it)
                update()
            }
            .launchIn(viewModelScope)

        releaseInteractor
            .observeFull(releaseId)
            .onEach {
                Log.e("kekeke", "observeFull")
                currentRelease = it
                update()
                updateProgress()
            }
            .launchIn(viewModelScope)
    }

    override fun onResume() {
        super.onResume()

        selectEpisodeJob?.cancel()
        selectEpisodeJob = playerController
            .selectEpisodeRelay
            .onEach { episodeId ->
                router.navigateTo(PlayerScreen(episodeId))
            }
            .launchIn(viewModelScope)
    }

    override fun onPause() {
        super.onPause()
        selectEpisodeJob?.cancel()
    }

    fun onContinueClick() {
        val releaseId = releaseId ?: return
        viewModelScope.launch {
            val defaultInstant = Instant.fromEpochMilliseconds(0)
            episodeHistoryRepository
                .getByRelease(releaseId)
                .maxByOrNull { it.lastOpenAt ?: defaultInstant }
                ?.also { visit ->
                    router.navigateTo(PlayerScreen(visit.id))
                }
        }
    }

    fun onPlayClick() {
        val releaseId = releaseId ?: return
        val episodes = currentRelease?.episodes ?: return
        if (episodes.isEmpty()) return

        viewModelScope.launch {
            if (episodes.size == 1) {
                router.navigateTo(PlayerScreen(episodes.first().id))
            } else {
                val defaultInstant = Instant.fromEpochMilliseconds(0)
                episodeHistoryRepository
                    .getByRelease(releaseId)
                    .maxByOrNull { it.lastOpenAt ?: defaultInstant }
                    ?.also { visit ->
                        guidedRouter.open(PlayerEpisodesGuidedScreen(visit.id))
                    }
            }
        }
    }

    fun onPlayWebClick() {

    }

    fun onFavoriteClick() {
        val release = currentRelease ?: return
        val favoriteInfo = release.favoriteInfo ?: return
        favoriteJob?.cancel()
        favoriteJob = viewModelScope.launch {
            if (authStateHolder.get() != AuthState.AUTH) {
                guidedRouter.open(AuthGuidedScreen())
                return@launch
            }
            runCatching {
                if (favoriteInfo.isAdded) {
                    favoriteRepository.deleteFavorite(release.id)
                } else {
                    favoriteRepository.addFavorite(release.id)
                }
            }.onSuccess {
                currentRelease = release.copy(favoriteInfo = it.favoriteInfo)
                update()
            }.onFailure {
                it.printStackTrace()
            }
            updateProgress()
        }

        updateProgress()
    }

    fun onDescriptionClick() {

    }

    //todo что-то подозрительное с updateprogress, нужно чекнуть шо норм отображается
    private fun updateProgress() {
        progressState.value = DetailsState(
            currentRelease == null,
            favoriteJob?.isActive == true
        )
    }

    private fun update() {
        val release = currentRelease ?: return
        releaseData.value = converter.toDetail(release, currentVisits)
    }
}