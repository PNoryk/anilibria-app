package ru.radiationx.anilibria.screen.details

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.radiationx.anilibria.common.BaseCardsViewModel
import ru.radiationx.anilibria.common.CardsDataConverter
import ru.radiationx.anilibria.common.LibriaCard
import ru.radiationx.anilibria.screen.DetailsScreen
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.ReleaseInteractor
import tv.anilibria.feature.content.data.repos.SearchRepository
import tv.anilibria.feature.content.types.ReleaseGenre
import tv.anilibria.feature.content.types.SearchForm
import tv.anilibria.feature.content.types.release.Release
import tv.anilibria.feature.content.types.release.ReleaseId

@InjectConstructor
class DetailRecommendsViewModel(
    private val releaseInteractor: ReleaseInteractor,
    private val searchRepository: SearchRepository,
    private val converter: CardsDataConverter,
    private val router: Router
) : BaseCardsViewModel() {

    var releaseId: ReleaseId? = null

    override val loadOnCreate: Boolean = false

    override val defaultTitle: String = "Рекомендации"

    override fun onCreate() {
        super.onCreate()

        cardsData.value = listOf(loadingCard)

        releaseInteractor
            .observeRelease(releaseId, null)
            .filterNotNull()
            .distinctUntilChanged()
            .onEach { onRefreshClick() }
            .launchIn(viewModelScope)
    }

    private suspend fun searchGenres(genresCount: Int, requestPage: Int): List<Release> =
        searchRepository
            .searchReleases(
                SearchForm(
                    genres = getGenres(genresCount),
                    sort = SearchForm.Sort.RATING
                ), requestPage
            )
            .let { result -> result.items.filter { it.id != releaseId } }

    override suspend fun getCoLoader(requestPage: Int): List<LibriaCard> {
        return searchGenres(3, requestPage)
            .ifEmpty {
                searchGenres(2, requestPage)
            }
            .let { result ->
                result.map { converter.toCard(it) }
            }
    }

    private suspend fun getGenres(count: Int): List<ReleaseGenre> {
        val release = releaseInteractor.awaitRelease(releaseId, null)
        return release.genres?.take(count).orEmpty()
    }

    override fun onLibriaCardClick(card: LibriaCard) {
        router.navigateTo(DetailsScreen(ReleaseId(card.id)))
    }
}