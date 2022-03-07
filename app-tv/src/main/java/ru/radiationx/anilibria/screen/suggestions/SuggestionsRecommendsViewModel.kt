package ru.radiationx.anilibria.screen.suggestions

import ru.radiationx.anilibria.common.BaseCardsViewModel
import ru.radiationx.anilibria.common.CardsDataConverter
import ru.radiationx.anilibria.common.LibriaCard
import ru.radiationx.anilibria.screen.DetailsScreen
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.feature.data.ReleaseInteractor
import tv.anilibria.feature.data.repos.SearchRepository
import tv.anilibria.feature.domain.entity.SearchForm
import tv.anilibria.feature.domain.entity.release.ReleaseId

@InjectConstructor
class SuggestionsRecommendsViewModel(
    private val searchRepository: SearchRepository,
    private val releaseInteractor: ReleaseInteractor,
    private val converter: CardsDataConverter,
    private val router: Router
) : BaseCardsViewModel() {

    override val defaultTitle: String = "Рекомендации"

    override val loadOnCreate: Boolean = false

    override fun onColdCreate() {
        super.onColdCreate()
        onRefreshClick()
    }

    override suspend fun getCoLoader(requestPage: Int): List<LibriaCard> = searchRepository
        .searchReleases(SearchForm(sort = SearchForm.Sort.RATING), requestPage)
        .also { releaseInteractor.updateItemsCache(it.items) }
        .let { result -> result.items.map { converter.toCard(it) } }

    override fun onLibriaCardClick(card: LibriaCard) {
        router.navigateTo(DetailsScreen(ReleaseId(card.id)))
    }
}