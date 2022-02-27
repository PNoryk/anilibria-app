package ru.radiationx.anilibria.presentation.search

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.model.SuggestionItemState
import ru.radiationx.anilibria.model.SuggestionLocalItemState
import ru.radiationx.anilibria.model.loading.StateController
import ru.radiationx.anilibria.model.toSuggestionState
import ru.radiationx.anilibria.navigation.Screens
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.shared_app.AppLinkHelper
import ru.terrakok.cicerone.Router
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.module.data.BaseUrlHelper
import tv.anilibria.module.data.analytics.AnalyticsConstants
import tv.anilibria.module.data.analytics.features.CatalogAnalytics
import tv.anilibria.module.data.analytics.features.FastSearchAnalytics
import tv.anilibria.module.data.analytics.features.ReleaseAnalytics
import tv.anilibria.module.data.repos.SearchRepository
import tv.anilibria.module.domain.entity.release.Release
import java.net.URLEncoder
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@InjectViewState
class FastSearchPresenter @Inject constructor(
    private val searchRepository: SearchRepository,
    private val router: Router,
    private val errorHandler: IErrorHandler,
    private val catalogAnalytics: CatalogAnalytics,
    private val fastSearchAnalytics: FastSearchAnalytics,
    private val releaseAnalytics: ReleaseAnalytics,
    private val urlHelper: BaseUrlHelper,
    private val appLinkHelper: AppLinkHelper
) : BasePresenter<FastSearchView>(router) {

    companion object {
        private const val ITEM_ID_SEARCH = -100
        private const val ITEM_ID_GOOGLE = -200
    }

    private val stateController = StateController(FastSearchScreenState())

    private var currentQuery = ""
    private var queryRelay = MutableSharedFlow<String>()
    private var currentSuggestions = mutableListOf<Release>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        stateController
            .observeState()
            .onEach { viewState.showState(it) }
            .launchIn(viewModelScope)

        queryRelay
            .debounce(350L.milliseconds)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.length >= 3) {
                    stateController.updateState {
                        it.copy(loading = true)
                    }
                } else {
                    showItems(emptyList(), query, false)
                }
            }
            .filter { it.length >= 3 }
            .mapLatest { query ->
                runCatching {
                    searchRepository.fastSearch(query)
                }.getOrNull() ?: emptyList()
            }
            .onEach {
                showItems(it, currentQuery)
            }
            .catch {
                errorHandler.handle(it)
                stateController.updateState {
                    it.copy(loading = false)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onClose() {
        currentQuery = ""
        showItems(emptyList(), currentQuery)
    }

    private fun showItems(items: List<Release>, query: String, appendEmpty: Boolean = true) {
        currentSuggestions.clear()
        currentSuggestions.addAll(items)

        val isNotFound = appendEmpty && currentSuggestions.isEmpty() && query.isNotEmpty()
        val stateItems = currentSuggestions.map { it.toSuggestionState(urlHelper, query) }
        val localItems = if (isNotFound) {
            listOf(
                SuggestionLocalItemState(
                    id = ITEM_ID_SEARCH,
                    icRes = R.drawable.ic_toolbar_search,
                    title = "Искать по жанрам и годам"
                ),
                SuggestionLocalItemState(
                    id = ITEM_ID_GOOGLE,
                    icRes = R.drawable.ic_google,
                    title = "Найти в гугле \"$query\""
                )
            )
        } else {
            emptyList()
        }

        stateController.updateState {
            it.copy(
                loading = false,
                localItems = localItems,
                items = stateItems
            )
        }
    }

    fun onQueryChange(query: String) {
        currentQuery = query
        viewModelScope.launch {
            queryRelay.emit(currentQuery)
        }
    }

    fun onItemClick(item: SuggestionItemState) {
        val suggestionItem = currentSuggestions.find { it.id == item.id } ?: return
        fastSearchAnalytics.releaseClick()
        releaseAnalytics.open(AnalyticsConstants.screen_fast_search, suggestionItem.id.id)
        router.navigateTo(Screens.ReleaseDetails(suggestionItem.id, suggestionItem.code))
    }

    fun onLocalItemClick(item: SuggestionLocalItemState) {
        when (item.id) {
            ITEM_ID_GOOGLE -> {
                fastSearchAnalytics.searchGoogleClick()
                val urlQuery = URLEncoder.encode("anilibria $currentQuery", "utf-8")
                appLinkHelper.openLink(AbsoluteUrl("https://www.google.com/search?q=$urlQuery"))
            }
            ITEM_ID_SEARCH -> {
                catalogAnalytics.open(AnalyticsConstants.screen_fast_search)
                fastSearchAnalytics.catalogClick()
                router.navigateTo(Screens.ReleasesSearch())
            }
        }
    }
}