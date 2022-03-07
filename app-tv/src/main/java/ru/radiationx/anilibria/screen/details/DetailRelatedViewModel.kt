package ru.radiationx.anilibria.screen.details

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.radiationx.anilibria.common.BaseCardsViewModel
import ru.radiationx.anilibria.common.CardsDataConverter
import ru.radiationx.anilibria.common.LibriaCard
import ru.radiationx.anilibria.screen.DetailsScreen
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.ReleaseInteractor
import tv.anilibria.feature.content.data.repos.ReleaseRepository
import tv.anilibria.feature.content.types.release.ReleaseId

@InjectConstructor
class DetailRelatedViewModel(
    private val releaseInteractor: ReleaseInteractor,
    private val releaseRepository: ReleaseRepository,
    private val converter: CardsDataConverter,
    private val router: Router
) : BaseCardsViewModel() {

    var releaseId: ReleaseId? = null

    override val loadOnCreate: Boolean = false

    override val defaultTitle: String = "Связанные релизы"

    override fun onCreate() {
        super.onCreate()

        cardsData.value = listOf(loadingCard)

        releaseInteractor
            .observeFull(releaseId)
            .distinctUntilChanged()
            .onEach {
                onRefreshClick()
            }
            .launchIn(viewModelScope)
    }

    override suspend fun getCoLoader(requestPage: Int): List<LibriaCard> {
        val release = releaseInteractor.getFull(releaseId) ?: releaseInteractor.getItem(releaseId)
        val releaseCodes =
            DetailsViewModel.getReleasesFromDesc(release?.description?.text.orEmpty())
        if (releaseCodes.isEmpty()) {
            return emptyList()
        }
        return releaseCodes
            .map { releaseRepository.getRelease(it) }
            .let { releases ->
                releaseInteractor.updateItemsCache(releases)
                Log.e("kekeke", "related releases ${releases.map { it.id }}")
                releases.map { converter.toCard(it) }
            }
    }

    override fun hasMoreCards(newCards: List<LibriaCard>, allCards: List<LibriaCard>): Boolean =
        false

    override fun onLibriaCardClick(card: LibriaCard) {
        router.navigateTo(DetailsScreen(ReleaseId(card.id)))
    }
}