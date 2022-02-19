package ru.radiationx.anilibria.screen.search

import androidx.lifecycle.MutableLiveData
import ru.radiationx.anilibria.common.BaseCardsViewModel
import ru.radiationx.anilibria.common.CardsDataConverter
import ru.radiationx.anilibria.common.LibriaCard
import ru.radiationx.anilibria.screen.DetailsScreen
import ru.radiationx.anilibria.screen.SuggestionsScreen
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.module.data.repos.SearchRepository
import tv.anilibria.module.domain.entity.SearchForm

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

        searchController.applyFormEvent.lifeSubscribe {
            searchForm = it
            onRefreshClick()
        }
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
            router.navigateTo(DetailsScreen(card.id))
        }
    }
}