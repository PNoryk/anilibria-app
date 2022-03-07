package ru.radiationx.anilibria.screen.watching

import kotlinx.datetime.Instant
import ru.radiationx.anilibria.common.BaseCardsViewModel
import ru.radiationx.anilibria.common.CardsDataConverter
import ru.radiationx.anilibria.common.LibriaCard
import ru.radiationx.anilibria.screen.DetailsScreen
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.feature.player.data.EpisodeHistoryRepository
import tv.anilibria.feature.content.data.repos.HistoryRepository
import tv.anilibria.feature.content.data.repos.ReleaseRepository
import tv.anilibria.feature.content.types.release.ReleaseId

@InjectConstructor
class WatchingContinueViewModel(
    private val historyRepository: HistoryRepository,
    private val episodesCheckerHolder: EpisodeHistoryRepository,
    private val releaseRepository: ReleaseRepository,
    private val converter: CardsDataConverter,
    private val router: Router
) : BaseCardsViewModel() {

    override val defaultTitle: String = "Продолжить просмотр"

    // todo ну это просто за гранью добра и зла, нужно зарефачить и оптимизировать
    override suspend fun getCoLoader(requestPage: Int): List<LibriaCard> = episodesCheckerHolder
        .getAll()
        .let { visits ->
            val defaultInstant = Instant.fromEpochMilliseconds(0)
            visits.sortedByDescending { visit -> visit.lastOpenAt ?: defaultInstant }
                .map { visit -> visit.id.releaseId }
                .toSet()
        }
        .let { releaseIds ->
            if (releaseIds.isEmpty()) {
                return@let emptyList()
            }
            historyRepository.getReleases().let { visits ->
                visits.filter { releaseIds.contains(it.id) }
            }
        }
        .let { visits ->
            val defaultInstant = Instant.fromEpochMilliseconds(0)
            visits.mapNotNull { visit ->
                val lastEpisode = episodesCheckerHolder.getByRelease(visit.id)
                    .maxByOrNull { it.lastOpenAt ?: defaultInstant }
                lastEpisode?.let { Pair(visit, it) }
            }
        }
        .let { visits ->
            val releases = releaseRepository.getReleasesById(visits.map { it.first.id })
            visits.mapNotNull { visit ->
                releases.find { it.id == visit.first.id }?.let {
                    Pair(it, visit.second)
                }
            }
        }
        .let {
            val defaultInstant = Instant.fromEpochMilliseconds(0)
            it.sortedByDescending { it.second.lastOpenAt ?: defaultInstant }
                .map {
                    converter.toCard(it.first)
                        .copy(description = "Вы остановились на ${it.second.id} серии")
                }
        }

    override fun hasMoreCards(newCards: List<LibriaCard>, allCards: List<LibriaCard>): Boolean =
        false

    override fun onLibriaCardClick(card: LibriaCard) {
        super.onLibriaCardClick(card)
        router.navigateTo(DetailsScreen(ReleaseId(card.id)))
    }
}