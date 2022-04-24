package ru.radiationx.anilibria.presentation.history

import android.util.Log
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.radiationx.anilibria.model.ReleaseItemState
import ru.radiationx.anilibria.model.loading.StateController
import ru.radiationx.anilibria.model.toState
import ru.radiationx.anilibria.navigation.Screens
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.ui.fragments.history.HistoryScreenState
import ru.radiationx.anilibria.utils.ShortcutHelper
import ru.radiationx.shared_app.AppLinkHelper
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.feature.analytics.api.AnalyticsConstants
import tv.anilibria.feature.analytics.api.features.HistoryAnalytics
import tv.anilibria.feature.analytics.api.features.ReleaseAnalytics
import tv.anilibria.feature.content.data.BaseUrlHelper
import tv.anilibria.feature.content.data.repos.HistoryRepository
import tv.anilibria.feature.content.data.repos.ReleaseRepository
import tv.anilibria.feature.content.types.release.Release
import tv.anilibria.feature.content.types.release.ReleaseId

@InjectViewState
@InjectConstructor
class HistoryPresenter(
    private val router: Router,
    private val historyRepository: HistoryRepository,
    private val releaseRepository: ReleaseRepository,
    private val historyAnalytics: HistoryAnalytics,
    private val releaseAnalytics: ReleaseAnalytics,
    private val shortcutHelper: ShortcutHelper,
    private val appLinkHelper: AppLinkHelper,
    private val urlHelper: BaseUrlHelper
) : BasePresenter<HistoryView>(router) {

    private val currentReleases = mutableListOf<Release>()
    private val stateController = StateController(HistoryScreenState())

    private var isSearchEnabled: Boolean = false
    private var currentQuery: String = ""

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        stateController
            .observeState()
            .onEach { viewState.showState(it) }
            .launchIn(viewModelScope)
        observeReleases()
    }

    private fun observeReleases() {
        historyRepository
            .observeReleases()
            .onEach { Log.d("kekeke","history $it") }
            .map { releaseRepository.getReleasesById(it.map { it.id }) }
            .onEach { releases ->
                currentReleases.clear()
                currentReleases.addAll(releases)

                stateController.updateState {
                    it.copy(items = currentReleases.map { it.toState(urlHelper) })
                }

                updateSearchState()
            }
            .launchIn(viewModelScope)
    }

    private fun updateSearchState() {
        isSearchEnabled = currentQuery.isNotEmpty()
        val searchItes = if (currentQuery.isNotEmpty()) {
            currentReleases.filter {
                it.nameRus?.text.orEmpty().contains(currentQuery, true)
                        || it.nameEng?.text.orEmpty().contains(currentQuery, true)
            }
        } else {
            emptyList()
        }
        stateController.updateState {
            it.copy(searchItems = searchItes.map { it.toState(urlHelper) })
        }
    }

    private fun findRelease(id: ReleaseId): Release? {
        return currentReleases.find { it.id == id }
    }

    fun localSearch(query: String) {
        currentQuery = query
        updateSearchState()
    }

    fun onItemClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        if (isSearchEnabled) {
            historyAnalytics.searchReleaseClick()
        } else {
            historyAnalytics.releaseClick()
        }
        releaseAnalytics.open(AnalyticsConstants.screen_history, releaseItem.id.id)
        router.navigateTo(Screens.ReleaseDetails(releaseItem.id, releaseItem.code))
    }

    fun onDeleteClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        historyAnalytics.releaseDeleteClick()
        viewModelScope.launch {
            historyRepository.removeRelease(releaseItem.id)
        }
    }

    fun onCopyClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        appLinkHelper.copyLink(releaseItem.link)
        releaseAnalytics.copyLink(AnalyticsConstants.screen_history, releaseItem.id.id)
    }

    fun onShareClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        appLinkHelper.shareLink(releaseItem.link)
        releaseAnalytics.share(AnalyticsConstants.screen_history, releaseItem.id.id)
    }

    fun onShortcutClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        shortcutHelper.addShortcut(releaseItem)
        releaseAnalytics.shortcut(AnalyticsConstants.screen_history, releaseItem.id.id)
    }

    fun onSearchClick() {
        historyAnalytics.searchClick()
    }
}