package ru.radiationx.anilibria.presentation.release.details

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import moxy.InjectViewState
import ru.radiationx.anilibria.model.loading.StateController
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.anilibria.ui.fragments.release.details.ReleasePagerState
import ru.radiationx.anilibria.utils.ShortcutHelper
import ru.radiationx.shared_app.AppLinkHelper
import ru.terrakok.cicerone.Router
import tv.anilibria.feature.content.data.BaseUrlHelper
import tv.anilibria.feature.content.data.ReleaseInteractor
import tv.anilibria.feature.content.data.analytics.AnalyticsConstants
import tv.anilibria.feature.content.data.analytics.features.CommentsAnalytics
import tv.anilibria.feature.content.data.analytics.features.ReleaseAnalytics
import tv.anilibria.feature.content.data.repos.HistoryRepository
import tv.anilibria.feature.domain.entity.release.Release
import tv.anilibria.feature.domain.entity.release.ReleaseCode
import tv.anilibria.feature.domain.entity.release.ReleaseId
import javax.inject.Inject

/* Created by radiationx on 18.11.17. */
@InjectViewState
class ReleasePresenter @Inject constructor(
    private val releaseInteractor: ReleaseInteractor,
    private val historyRepository: HistoryRepository,
    private val router: Router,
    private val errorHandler: IErrorHandler,
    private val commentsAnalytics: CommentsAnalytics,
    private val releaseAnalytics: ReleaseAnalytics,
    private val appLinkHelper: AppLinkHelper,
    private val shortcutHelper: ShortcutHelper,
    private val urlHelper: BaseUrlHelper
) : BasePresenter<ReleaseView>(router) {

    private var currentData: Release? = null
    var releaseId: ReleaseId? = null
    var releaseIdCode: ReleaseCode? = null

    private val stateController = StateController(ReleasePagerState())

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        releaseInteractor.getItem(releaseId, releaseIdCode)?.also {
            updateLocalRelease(it)
        }
        observeRelease()
        loadRelease()

        stateController
            .observeState()
            .onEach { viewState.showState(it) }
            .launchIn(viewModelScope)
    }

    private fun loadRelease() {
        releaseInteractor
            .loadRelease(releaseId, releaseIdCode)
            .take(1)
            .onEach {
                viewState.setRefreshing(false)
                historyRepository.putRelease(it.id)
            }
            .catch {
                viewState.setRefreshing(false)
                errorHandler.handle(it)
            }
            .launchIn(viewModelScope)
    }

    private fun observeRelease() {
        releaseInteractor
            .observeFull(releaseId, releaseIdCode)
            .onEach { release ->
                updateLocalRelease(release)
                historyRepository.putRelease(release.id)
            }
            .catch {
                errorHandler.handle(it)
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
