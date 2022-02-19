package ru.radiationx.anilibria.screen.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
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
import ru.radiationx.data.entity.common.AuthState
import ru.radiationx.data.repository.AuthRepository
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.module.data.ReleaseInteractor
import tv.anilibria.module.data.repos.EpisodeHistoryRepository
import tv.anilibria.module.data.repos.FavoriteRepository
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.module.domain.entity.release.ReleaseId

@InjectConstructor
class DetailHeaderViewModel(
    private val releaseInteractor: ReleaseInteractor,
    private val favoriteRepository: FavoriteRepository,
    private val episodeHistoryRepository: EpisodeHistoryRepository,
    private val authRepository: AuthRepository,
    private val converter: DetailDataConverter,
    private val router: Router,
    private val guidedRouter: GuidedRouter,
    private val playerController: PlayerController
) : LifecycleViewModel() {

    var releaseId: ReleaseId? = null

    val releaseData = MutableLiveData<LibriaDetails>()
    val progressState = MutableLiveData<DetailsState>()

    private var currentRelease: Release? = null

    private var selectEpisodeDisposable = Disposables.disposed()
    private var favoriteDisposable: Job? = null

    override fun onCreate() {
        super.onCreate()

        (releaseInteractor.getFull(releaseId) ?: releaseInteractor.getItem(releaseId))?.also {
            currentRelease = it
            update(it)
        }
        updateProgress()

        releaseInteractor
            .observeFull(releaseId)
            .onEach {
                Log.e("kekeke", "observeFull")
                currentRelease = it
                update(it)
                updateProgress()
            }
            .launchIn(viewModelScope)
    }

    override fun onResume() {
        super.onResume()

        selectEpisodeDisposable.dispose()
        selectEpisodeDisposable = playerController
            .selectEpisodeRelay
            .observeOn(AndroidSchedulers.mainThread())
            .lifeSubscribe { episodeId ->
                router.navigateTo(PlayerScreen(episodeId))
            }
    }

    override fun onPause() {
        super.onPause()
        selectEpisodeDisposable.dispose()
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
        if (authRepository.getAuthState() != AuthState.AUTH) {
            guidedRouter.open(AuthGuidedScreen())
            return
        }

        favoriteDisposable?.cancel()
        favoriteDisposable = viewModelScope.launch {
            runCatching {
                if (favoriteInfo.isAdded) {
                    favoriteRepository.deleteFavorite(release.id)
                } else {
                    favoriteRepository.addFavorite(release.id)
                }
            }.onSuccess {
                val newRelease = release.copy(favoriteInfo = it.favoriteInfo)
                update(newRelease)
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
            favoriteDisposable?.isActive == true
        )
    }

    private fun update(release: Release) {
        releaseData.value = converter.toDetail(release)
    }
}