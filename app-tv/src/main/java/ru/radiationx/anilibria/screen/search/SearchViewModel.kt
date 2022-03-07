package ru.radiationx.anilibria.screen.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.radiationx.anilibria.common.BaseCardsViewModel
import ru.radiationx.anilibria.common.CardsDataConverter
import ru.radiationx.anilibria.common.LibriaCard
import ru.radiationx.anilibria.screen.DetailsScreen
import ru.radiationx.anilibria.screen.SuggestionsScreen
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.repos.SearchRepository
import tv.anilibria.feature.domain.entity.SearchForm
import tv.anilibria.feature.domain.entity.release.ReleaseId

@InjectConstructor
class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val converter: CardsDataConverter,
    private val router: Router,
    private val searchController: SearchController
) : BaseCardsViewModel() {

    private var searchForm = SearchForm()

    val progressState = MutableLiveData<Boolean>()

    override val loadOnCreate: Boolean = false

    override val progressOnRefresh: Boolean = false

    override fun onColdCreate() {
        super.onColdCreate()

        searchController.applyFormEvent.onEach {
            searchForm = it
            onRefreshClick()
        }.launchIn(viewModelScope)
    }

    override suspend fun getCoLoader(requestPage: Int): List<LibriaCard> = searchRepository
        .searchReleases(searchForm, requestPage)
        .let { it.items.map { converter.toCard(it) } }
        .also { progressState.value = false }

    fun onSearchClick() {
        router.navigateTo(SuggestionsScreen())
    }

    override fun onLibriaCardClick(card: LibriaCard) {
        if (card.type == LibriaCard.Type.RELEASE) {
            router.navigateTo(DetailsScreen(ReleaseId(card.id)))
        }
    }
}