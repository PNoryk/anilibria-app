package ru.radiationx.anilibria.presentation.favorites

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.radiationx.shared_app.AppLinkHelper
import ru.radiationx.anilibria.model.ReleaseItemState
import ru.radiationx.anilibria.model.loading.DataLoadingController
import ru.radiationx.anilibria.model.loading.PageLoadParams
import ru.radiationx.anilibria.model.loading.ScreenStateAction
import ru.radiationx.anilibria.model.loading.StateController
import ru.radiationx.anilibria.model.toState
import ru.radiationx.anilibria.navigation.Screens
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.anilibria.ui.fragments.favorites.FavoritesScreenState
import ru.radiationx.anilibria.utils.ShortcutHelper
import ru.terrakok.cicerone.Router
import tv.anilibria.module.data.UrlHelper
import tv.anilibria.module.data.analytics.AnalyticsConstants
import tv.anilibria.module.data.analytics.features.FavoritesAnalytics
import tv.anilibria.module.data.analytics.features.ReleaseAnalytics
import tv.anilibria.module.data.repos.FavoriteRepository
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.module.domain.entity.release.ReleaseId
import javax.inject.Inject

/**
 * Created by radiationx on 13.01.18.
 */
@InjectViewState
class FavoritesPresenter @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val router: Router,
    private val errorHandler: IErrorHandler,
    private val favoritesAnalytics: FavoritesAnalytics,
    private val releaseAnalytics: ReleaseAnalytics,
    private val appLinkHelper: AppLinkHelper,
    private val shortcutHelper: ShortcutHelper,
    private val urlHelper: UrlHelper
) : BasePresenter<FavoritesView>(router) {

    private val loadingController = DataLoadingController(viewModelScope) {
        submitPageAnalytics(it.page)
        getDataSource(it)
    }

    private val stateController = StateController(FavoritesScreenState())

    private var lastLoadedPage: Int? = null
    private var isSearchEnabled: Boolean = false
    private var currentQuery: String = ""

    private val currentReleases = mutableListOf<Release>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        stateController
            .observeState()
            .onEach { viewState.showState(it) }
            .launchIn(viewModelScope)

        loadingController
            .observeState()
            .onEach { loadingData ->
                stateController.updateState {
                    it.copy(data = loadingData)
                }
                updateSearchState()
            }
            .launchIn(viewModelScope)

        refreshReleases()
    }

    fun refreshReleases() {
        loadingController.refresh()
    }

    fun loadMore() {
        loadingController.loadMore()
    }

    fun deleteFav(id: ReleaseId) {
        viewModelScope.launch {
            favoritesAnalytics.deleteFav()
            stateController.updateState {
                it.copy(deletingItemIds = it.deletingItemIds + id)
            }

            runCatching {
                favoriteRepository.deleteFavorite(id)
            }.onSuccess { deletedItem ->
                loadingController.currentState.data?.also { dataState ->
                    val newItems = dataState.toMutableList()
                    newItems.find { it.id == deletedItem.id }?.also {
                        newItems.remove(it)
                    }
                    loadingController.modifyData(newItems)
                }
            }.onFailure {
                errorHandler.handle(it)
            }

            stateController.updateState {
                it.copy(deletingItemIds = it.deletingItemIds - id)
            }
        }
    }

    fun onCopyClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        appLinkHelper.copyLink(releaseItem.link)
        releaseAnalytics.copyLink(AnalyticsConstants.screen_favorites, releaseItem.id.id)
    }

    fun onShareClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        appLinkHelper.shareLink(releaseItem.link)
        releaseAnalytics.share(AnalyticsConstants.screen_favorites, releaseItem.id.id)
    }

    fun onShortcutClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        shortcutHelper.addShortcut(releaseItem)
        releaseAnalytics.shortcut(AnalyticsConstants.screen_favorites, releaseItem.id.id)
    }

    fun localSearch(query: String) {
        currentQuery = query
        updateSearchState()
    }

    fun onSearchClick() {
        favoritesAnalytics.searchClick()
    }

    fun onItemClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        if (isSearchEnabled) {
            favoritesAnalytics.searchReleaseClick()
        } else {
            favoritesAnalytics.releaseClick()
        }
        releaseAnalytics.open(AnalyticsConstants.screen_favorites, releaseItem.id.id)
        router.navigateTo(Screens.ReleaseDetails(releaseItem.id, releaseItem.code))
    }

    private fun findRelease(id: ReleaseId): Release? {
        return currentReleases.find { it.id == id }
    }

    private fun submitPageAnalytics(page: Int) {
        if (lastLoadedPage != page) {
            favoritesAnalytics.loadPage(page)
            lastLoadedPage = page
        }
    }

    private suspend fun getDataSource(params: PageLoadParams): ScreenStateAction.Data<List<ReleaseItemState>> {
        return try {
            val paginated = favoriteRepository.getFavorites(params.page)
            if (params.isFirstPage) {
                currentReleases.clear()
            }
            currentReleases.addAll(paginated.items)
            val newItems = currentReleases.map { it.toState(urlHelper) }
            ScreenStateAction.Data(newItems, !paginated.meta.isEnd())
        } catch (ex: Exception) {
            if (params.isFirstPage) {
                errorHandler.handle(ex)
            }
            throw ex
        }
    }

    private fun updateSearchState() {
        isSearchEnabled = currentQuery.isNotEmpty()
        val searchItems = if (currentQuery.isNotEmpty()) {
            currentReleases.filter {
                it.nameRus?.text.orEmpty().contains(currentQuery, true)
                        || it.nameEng?.text.orEmpty().contains(currentQuery, true)
            }
        } else {
            emptyList()
        }
        val newItems = searchItems.map { it.toState(urlHelper) }
        stateController.updateState {
            it.copy(searchItems = newItems)
        }
    }

}