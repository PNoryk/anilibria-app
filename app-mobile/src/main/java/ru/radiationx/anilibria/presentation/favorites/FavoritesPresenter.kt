package ru.radiationx.anilibria.presentation.favorites

import io.reactivex.Single
import moxy.InjectViewState
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
import ru.radiationx.anilibria.utils.Utils
import ru.radiationx.data.analytics.AnalyticsConstants
import ru.radiationx.data.analytics.features.FavoritesAnalytics
import ru.radiationx.data.analytics.features.ReleaseAnalytics
import ru.radiationx.data.entity.app.release.ReleaseItem
import ru.radiationx.data.repository.FavoriteRepository
import ru.terrakok.cicerone.Router
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
    private val releaseAnalytics: ReleaseAnalytics
) : BasePresenter<FavoritesView>(router) {

    private val loadingController = DataLoadingController {
        submitPageAnalytics(it.page)
        getDataSource(it)
    }.addToDisposable()

    private val stateController = StateController(FavoritesScreenState())

    private var lastLoadedPage: Int? = null
    private var isSearchEnabled: Boolean = false
    private var currentQuery: String = ""

    private val currentReleases = mutableListOf<ReleaseItem>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        stateController
            .observeState()
            .subscribe { viewState.showState(it) }
            .addToDisposable()

        loadingController
            .observeState()
            .subscribe { loadingData ->
                stateController.updateState {
                    it.copy(data = loadingData)
                }
                updateSearchState()
            }
            .addToDisposable()

        refreshReleases()
    }

    fun refreshReleases() {
        loadingController.refresh()
    }

    fun loadMore() {
        loadingController.loadMore()
    }

    fun deleteFav(id: Int) {
        favoritesAnalytics.deleteFav()
        stateController.updateState {
            it.copy(deletingItemIds = it.deletingItemIds + id)
        }
        favoriteRepository
            .deleteFavorite(id)
            .doFinally {
                stateController.updateState {
                    it.copy(deletingItemIds = it.deletingItemIds - id)
                }
            }
            .subscribe({ deletedItem ->
                loadingController.currentState.data?.also { dataState ->
                    val newItems = dataState.toMutableList()
                    newItems.find { it.id == deletedItem.id }?.also {
                        newItems.remove(it)
                    }
                    loadingController.modifyData(newItems)
                }
            }) { throwable ->
                errorHandler.handle(throwable)
            }
            .addToDisposable()
    }

    fun onCopyClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        Utils.copyToClipBoard(releaseItem.link.orEmpty())
        releaseAnalytics.copyLink(AnalyticsConstants.screen_favorites, releaseItem.id)
    }

    fun onShareClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        Utils.shareText(releaseItem.link.orEmpty())
        releaseAnalytics.share(AnalyticsConstants.screen_favorites, releaseItem.id)
    }

    fun onShortcutClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        ShortcutHelper.addShortcut(releaseItem)
        releaseAnalytics.shortcut(AnalyticsConstants.screen_favorites, releaseItem.id)
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
        releaseAnalytics.open(AnalyticsConstants.screen_favorites, releaseItem.id)
        router.navigateTo(Screens.ReleaseDetails(releaseItem.id, releaseItem.code, releaseItem))
    }

    private fun findRelease(id: Int): ReleaseItem? {
        return currentReleases.find { it.id == id }
    }

    private fun submitPageAnalytics(page: Int) {
        if (lastLoadedPage != page) {
            favoritesAnalytics.loadPage(page)
            lastLoadedPage = page
        }
    }

    private fun getDataSource(params: PageLoadParams): Single<ScreenStateAction.Data<List<ReleaseItemState>>> {
        return favoriteRepository
            .getFavorites(params.page)
            .map { paginated ->
                if (params.isFirstPage) {
                    currentReleases.clear()
                }
                currentReleases.addAll(paginated.data)
                val newItems = currentReleases.map { it.toState() }
                ScreenStateAction.Data(newItems, !paginated.isEnd())
            }
            .doOnError {
                if (params.isFirstPage) {
                    errorHandler.handle(it)
                }
            }
    }

    private fun updateSearchState() {
        isSearchEnabled = currentQuery.isNotEmpty()
        val searchItems = if (currentQuery.isNotEmpty()) {
            currentReleases.filter {
                it.title.orEmpty().contains(currentQuery, true)
                        || it.titleEng.orEmpty().contains(currentQuery, true)
            }
        } else {
            emptyList()
        }
        val newItems = searchItems.map { it.toState() }
        stateController.updateState {
            it.copy(searchItems = newItems)
        }
    }

}