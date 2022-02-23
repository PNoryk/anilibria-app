package ru.radiationx.anilibria.presentation.search

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.radiationx.anilibria.AppLinkHelper
import ru.radiationx.anilibria.model.ReleaseItemState
import ru.radiationx.anilibria.model.loading.DataLoadingController
import ru.radiationx.anilibria.model.loading.PageLoadParams
import ru.radiationx.anilibria.model.loading.ScreenStateAction
import ru.radiationx.anilibria.model.loading.StateController
import ru.radiationx.anilibria.model.toState
import ru.radiationx.anilibria.navigation.Screens
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.anilibria.ui.fragments.search.SearchScreenState
import ru.radiationx.anilibria.utils.ShortcutHelper
import ru.radiationx.anilibria.utils.Utils
import ru.terrakok.cicerone.Router
import tv.anilibria.module.data.UrlHelper
import tv.anilibria.module.data.analytics.AnalyticsConstants
import tv.anilibria.module.data.analytics.features.CatalogAnalytics
import tv.anilibria.module.data.analytics.features.CatalogFilterAnalytics
import tv.anilibria.module.data.analytics.features.FastSearchAnalytics
import tv.anilibria.module.data.analytics.features.ReleaseAnalytics
import tv.anilibria.module.data.preferences.PreferencesStorage
import tv.anilibria.module.data.repos.SearchRepository
import tv.anilibria.module.domain.entity.ReleaseGenre
import tv.anilibria.module.domain.entity.ReleaseSeason
import tv.anilibria.module.domain.entity.ReleaseYear
import tv.anilibria.module.domain.entity.SearchForm
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.module.domain.entity.release.ReleaseId
import tv.anilibria.plugin.data.analytics.TimeCounter
import javax.inject.Inject

@InjectViewState
class SearchPresenter @Inject constructor(
    private val searchRepository: SearchRepository,
    private val router: Router,
    private val errorHandler: IErrorHandler,
    private val catalogAnalytics: CatalogAnalytics,
    private val catalogFilterAnalytics: CatalogFilterAnalytics,
    private val fastSearchAnalytics: FastSearchAnalytics,
    private val releaseAnalytics: ReleaseAnalytics,
    private val preferencesStorage: PreferencesStorage,
    private val shortcutHelper: ShortcutHelper,
    private val appLinkHelper: AppLinkHelper,
    private val urlHelper: UrlHelper
) : BasePresenter<SearchCatalogView>(router) {


    private var lastLoadedPage: Int? = null
    private val filterUseTimeCounter = TimeCounter()

    private val remindText =
        "Если не удаётся найти нужный релиз, попробуйте искать через Google или Yandex c приставкой \"AniLibria\".\nПо ссылке в поисковике можно будет открыть приложение."

    private val currentGenres = mutableListOf<ReleaseGenre>()
    private val currentYears = mutableListOf<ReleaseYear>()
    private val currentSeasons = mutableListOf<ReleaseSeason>()
    private var currentSorting = SearchForm.Sort.DATE
    private var currentComplete = false
    private val currentItems = mutableListOf<Release>()

    private val beforeOpenDialogGenres = mutableListOf<ReleaseGenre>()
    private val beforeOpenDialogYears = mutableListOf<ReleaseYear>()
    private val beforeOpenDialogSeasons = mutableListOf<ReleaseSeason>()
    private var beforeOpenDialogSorting = SearchForm.Sort.DATE
    private var beforeComplete = false

    private val loadingController = DataLoadingController(viewModelScope) {
        submitPageAnalytics(it.page)
        getDataSource(it)
    }
    private val stateController = StateController(SearchScreenState())

    private fun findRelease(id: ReleaseId): Release? {
        return currentItems.find { it.id == id }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        observeScreenState()
        loadSeasons()
        loadGenres()
        loadYears()
        observeGenres()
        observeYears()
        observeSearchRemind()
        updateInfo()
        onChangeSorting(currentSorting)
        onChangeComplete(currentComplete)
        observeLoadingState()
        loadingController.refresh()
    }

    private fun loadSeasons() {
        viewModelScope.launch {
            runCatching {
                val seasons = searchRepository.getSeasons()
                viewState.showSeasons(seasons)
            }.onFailure {
                errorHandler.handle(it)
            }
        }
    }

    private fun loadGenres() {
        viewModelScope.launch {
            runCatching {
                searchRepository.getGenres()
            }.onFailure {
                errorHandler.handle(it)
            }
        }
    }

    private fun observeGenres() {
        searchRepository
            .observeGenres()
            .onEach { viewState.showGenres(it) }
            .catch { errorHandler.handle(it) }
            .launchIn(viewModelScope)
    }

    private fun loadYears() {
        viewModelScope.launch {
            runCatching {
                searchRepository.getYears()
            }.onFailure {
                errorHandler.handle(it)
            }
        }
    }

    private fun observeYears() {
        searchRepository
            .observeYears()
            .onEach { viewState.showYears(it) }
            .catch { errorHandler.handle(it) }
            .launchIn(viewModelScope)
    }

    private fun observeSearchRemind() {
        preferencesStorage
            .searchRemind.observe()
            .onEach { remindEnabled ->
                val newRemindText = remindText.takeIf { remindEnabled }
                stateController.updateState {
                    it.copy(remindText = newRemindText)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeLoadingState() {
        loadingController
            .observeState()
            .onEach { loadingData ->
                stateController.updateState {
                    it.copy(data = loadingData)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeScreenState() {
        stateController
            .observeState()
            .onEach { viewState.showState(it) }
            .launchIn(viewModelScope)
    }

    private fun submitPageAnalytics(page: Int) {
        if (lastLoadedPage != page) {
            catalogAnalytics.loadPage(page)
            lastLoadedPage = page
        }
    }

    private suspend fun getDataSource(params: PageLoadParams): ScreenStateAction.Data<List<ReleaseItemState>> {
        return try {
            val form = SearchForm(
                years = currentYears.toList(),
                seasons = currentSeasons.toList(),
                genres = currentGenres.toList(),
                sort = currentSorting,
                currentComplete
            )
            searchRepository
                .searchReleases(form, params.page)
                .let { paginated ->
                    if (params.isFirstPage) {
                        currentItems.clear()
                    }
                    currentItems.addAll(paginated.items)

                    val newItems = currentItems.map { it.toState(urlHelper) }
                    ScreenStateAction.Data(newItems, paginated.items.isNotEmpty())
                }
        } catch (ex: Exception) {
            if (params.isFirstPage) {
                errorHandler.handle(ex)
            }
            throw ex
        }
    }

    fun refreshReleases() {
        loadingController.refresh()
    }

    fun loadMore() {
        loadingController.loadMore()
    }

    fun showDialog() {
        filterUseTimeCounter.start()
        catalogAnalytics.filterClick()
        catalogFilterAnalytics.open(AnalyticsConstants.screen_catalog)
        beforeOpenDialogGenres.clear()
        beforeOpenDialogYears.clear()
        beforeOpenDialogSeasons.clear()
        beforeOpenDialogGenres.addAll(currentGenres)
        beforeOpenDialogYears.addAll(currentYears)
        beforeOpenDialogSeasons.addAll(currentSeasons)
        beforeOpenDialogSorting = currentSorting
        beforeComplete = currentComplete
        viewState.showDialog()
    }

    fun onAcceptDialog() {
        catalogFilterAnalytics.applyClick()
        if (
            beforeOpenDialogGenres != currentGenres
            || beforeOpenDialogYears != currentYears
            || beforeOpenDialogSeasons != currentSeasons
            || beforeOpenDialogSorting != currentSorting
            || beforeComplete != currentComplete
        ) {
            refreshReleases()
        }
    }

    fun onCloseDialog() {
        catalogFilterAnalytics.useTime(filterUseTimeCounter.elapsed())
        onAcceptDialog()
    }

    fun onChangeGenres(newGenres: List<ReleaseGenre>) {
        currentGenres.clear()
        currentGenres.addAll(newGenres)
        viewState.selectGenres(currentGenres)
        updateInfo()
    }

    fun onChangeYears(newYears: List<ReleaseYear>) {
        currentYears.clear()
        currentYears.addAll(newYears)
        viewState.selectYears(currentYears)
        updateInfo()
    }

    fun onChangeSeasons(newSeasons: List<ReleaseSeason>) {
        currentSeasons.clear()
        currentSeasons.addAll(newSeasons)
        viewState.selectSeasons(currentSeasons)
        updateInfo()
    }

    fun onChangeSorting(newSorting: SearchForm.Sort) {
        currentSorting = newSorting
        viewState.setSorting(currentSorting)
        updateInfo()
    }

    fun onChangeComplete(complete: Boolean) {
        currentComplete = complete
        viewState.setComplete(currentComplete)
        updateInfo()
    }

    private fun updateInfo() {
        viewState.updateInfo(
            currentSorting,
            currentGenres.size + currentYears.size + currentSeasons.size
        )
    }

    fun onFastSearchClick() {
        catalogAnalytics.fastSearchClick()
    }

    fun onFastSearchOpen() {
        fastSearchAnalytics.open(AnalyticsConstants.screen_catalog)
    }

    fun onRemindClose() {
        preferencesStorage.searchRemind.blockingSet(false)
    }

    fun onItemClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        catalogAnalytics.releaseClick()
        releaseAnalytics.open(AnalyticsConstants.screen_catalog, releaseItem.id.id)
        router.navigateTo(Screens.ReleaseDetails(releaseItem.id, releaseItem.code))
    }

    fun onCopyClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        appLinkHelper.copyLink(releaseItem.link)
        releaseAnalytics.copyLink(AnalyticsConstants.screen_catalog, releaseItem.id.id)
    }

    fun onShareClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        appLinkHelper.shareLink(releaseItem.link)
        releaseAnalytics.share(AnalyticsConstants.screen_catalog, releaseItem.id.id)
    }

    fun onShortcutClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        shortcutHelper.addShortcut(releaseItem)
        releaseAnalytics.shortcut(AnalyticsConstants.screen_catalog, releaseItem.id.id)
    }
}
