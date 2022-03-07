package ru.radiationx.anilibria.screen.watching

import ru.radiationx.anilibria.common.BaseCardsViewModel
import ru.radiationx.anilibria.common.CardsDataConverter
import ru.radiationx.anilibria.common.LibriaCard
import ru.radiationx.anilibria.screen.DetailsScreen
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.ReleaseInteractor
import tv.anilibria.feature.content.data.repos.HistoryRepository
import tv.anilibria.feature.content.data.repos.ReleaseRepository
import tv.anilibria.feature.content.data.repos.SearchRepository
import tv.anilibria.feature.content.types.ReleaseGenre
import tv.anilibria.feature.content.types.SearchForm
import tv.anilibria.feature.content.types.release.ReleaseId

@InjectConstructor
class WatchingRecommendsViewModel(
    private val historyRepository: HistoryRepository,
    private val searchRepository: SearchRepository,
    private val releaseInteractor: ReleaseInteractor,
    private val releaseRepository: ReleaseRepository,
    private val converter: CardsDataConverter,
    private val router: Router
) : BaseCardsViewModel() {

    override val defaultTitle: String = "Рекомендации"

    override val loadOnCreate: Boolean = false

    override fun onColdCreate() {
        super.onColdCreate()
        onRefreshClick()
    }

    override suspend fun getCoLoader(requestPage: Int): List<LibriaCard> {
        return historyRepository
            .getReleases()
            .let { releaseRepository.getReleasesById(it.map { it.id }) }
            .let { releases ->
                val genresMap = mutableMapOf<ReleaseGenre, Int>()
                releases.forEach { release ->
                    release.genres?.forEach {
                        val currentCount = genresMap[it] ?: 0
                        genresMap[it] = currentCount + 1
                    }
                }
                genresMap.toList().sortedByDescending { it.second }.take(3).map { it.first }
            }
            .let { genres ->
                val form = SearchForm(
                    genres = genres,
                    sort = SearchForm.Sort.RATING
                )
                searchRepository.searchReleases(form, requestPage)
            }
            .also {
                releaseInteractor.updateItemsCache(it.items)
            }
            .let { result ->
                result.items.map { converter.toCard(it) }
            }
    }

    override fun onLibriaCardClick(card: LibriaCard) {
        router.navigateTo(DetailsScreen(ReleaseId(card.id)))
    }

}