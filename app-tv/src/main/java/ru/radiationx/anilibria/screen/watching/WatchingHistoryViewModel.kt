package ru.radiationx.anilibria.screen.watching

import ru.radiationx.anilibria.common.BaseCardsViewModel
import ru.radiationx.anilibria.common.CardsDataConverter
import ru.radiationx.anilibria.common.LibriaCard
import ru.radiationx.anilibria.screen.DetailsScreen
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.module.data.repos.HistoryRepository
import tv.anilibria.module.data.repos.ReleaseRepository

@InjectConstructor
class WatchingHistoryViewModel(
    private val historyRepository: HistoryRepository,
    private val releaseRepository: ReleaseRepository,
    private val converter: CardsDataConverter,
    private val router: Router
) : BaseCardsViewModel() {

    override val defaultTitle: String = "История"

    override suspend fun getCoLoader(requestPage: Int): List<LibriaCard> = historyRepository
        .getReleases()
        .let { visits ->
            releaseRepository.getReleasesById(visits.map { it.id })
        }
        .let { historyItems ->
            historyItems.map { converter.toCard(it) }
        }

    override fun hasMoreCards(newCards: List<LibriaCard>, allCards: List<LibriaCard>): Boolean =
        false

    override fun onLibriaCardClick(card: LibriaCard) {
        router.navigateTo(DetailsScreen(card.id))
    }
}