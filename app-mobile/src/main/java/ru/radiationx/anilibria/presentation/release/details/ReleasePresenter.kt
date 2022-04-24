package ru.radiationx.anilibria.presentation.release.details

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.radiationx.anilibria.model.loading.StateController
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.anilibria.ui.fragments.release.details.ReleasePagerState
import ru.radiationx.anilibria.utils.ShortcutHelper
import ru.radiationx.shared_app.AppLinkHelper
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.feature.analytics.api.AnalyticsConstants
import tv.anilibria.feature.analytics.api.features.CommentsAnalytics
import tv.anilibria.feature.analytics.api.features.ReleaseAnalytics
import tv.anilibria.feature.auth.data.AuthStateHolder
import tv.anilibria.feature.content.data.BaseUrlHelper
import tv.anilibria.feature.content.data.repos.ReleaseCacheRepository
import tv.anilibria.feature.content.data.repos.HistoryRepository
import tv.anilibria.feature.content.data.repos.ReleaseRepository
import tv.anilibria.feature.content.types.release.Release
import tv.anilibria.feature.content.types.release.ReleaseCode
import tv.anilibria.feature.content.types.release.ReleaseId

@InjectViewState
@InjectConstructor
class ReleasePresenter(
    private val releaseCacheRepository: ReleaseCacheRepository,
    private val releaseRepository: ReleaseRepository,
    private val historyRepository: HistoryRepository,
    private val router: Router,
    private val errorHandler: IErrorHandler,
    private val commentsAnalytics: CommentsAnalytics,
    private val releaseAnalytics: ReleaseAnalytics,
    private val appLinkHelper: AppLinkHelper,
    private val shortcutHelper: ShortcutHelper,
    private val urlHelper: BaseUrlHelper,
    private val authStateHolder: AuthStateHolder
) : BasePresenter<ReleaseView>(router) {

    private var currentData: Release? = null
    var releaseId: ReleaseId? = null
    var releaseIdCode: ReleaseCode? = null

    private val stateController = StateController(ReleasePagerState())

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        observeRelease()
        loadRelease()
        subscribeAuth()

        stateController
            .observeState()
            .onEach { viewState.showState(it) }
            .launchIn(viewModelScope)
    }


    private fun subscribeAuth() {
        authStateHolder
            .observe()
            .distinctUntilChanged()
            .drop(1)
            .onEach {
                loadRelease()
            }
            .launchIn(viewModelScope)
    }

    private fun loadRelease() {
        viewModelScope.launch {
            runCatching {
                releaseRepository.getRelease(releaseId, releaseIdCode)
            }.onSuccess {
                viewState.setRefreshing(false)
                historyRepository.putRelease(it.id)
            }.onFailure {
                viewState.setRefreshing(false)
                errorHandler.handle(it)
            }
        }
    }

    private fun observeRelease() {
        releaseCacheRepository
            .observeRelease(releaseId, releaseIdCode)
            .filterNotNull()
            .onEach { release ->
                updateLocalRelease(release)
            }
            .launchIn(viewModelScope)
    }

    private fun updateLocalRelease(release: Release) {
        currentData = release
        releaseId = release.id
        releaseIdCode = release.code

        stateController.updateState {
            it.copy(
                poster = urlHelper.makeMedia(currentData?.poster),
                title = currentData?.let {
                    String.format("%s / %s", release.nameRus?.text, release.nameEng?.text)
                }
            )
        }
    }

    fun onShareClick() {
        currentData?.let {
            releaseAnalytics.share(AnalyticsConstants.screen_release, it.id.id)
            appLinkHelper.shareLink(it.link)
        }
    }

    fun onCopyLinkClick() {
        currentData?.let {
            releaseAnalytics.copyLink(AnalyticsConstants.screen_release, it.id.id)
            appLinkHelper.copyLink(it.link)
        }
    }

    fun onShortcutAddClick() {
        currentData?.let {
            releaseAnalytics.shortcut(AnalyticsConstants.screen_release, it.id.id)
            shortcutHelper.addShortcut(it)
        }
    }

    fun onCommentsSwipe() {
        currentData?.also {
            commentsAnalytics.open(AnalyticsConstants.screen_release, it.id.id)
        }
    }

}
