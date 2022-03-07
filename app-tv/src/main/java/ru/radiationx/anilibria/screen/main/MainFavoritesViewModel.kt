package ru.radiationx.anilibria.screen.main

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.BaseCardsViewModel
import ru.radiationx.anilibria.common.CardsDataConverter
import ru.radiationx.anilibria.common.LibriaCard
import ru.radiationx.anilibria.screen.DetailsScreen
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.feature.auth.data.AuthStateHolder
import tv.anilibria.feature.auth.data.domain.AuthState
import tv.anilibria.feature.content.data.repos.FavoriteRepository
import tv.anilibria.feature.content.types.release.ReleaseId

@InjectConstructor
class MainFavoritesViewModel(
    private val favoriteRepository: FavoriteRepository,
    private val authStateHolder: AuthStateHolder,
    private val converter: CardsDataConverter,
    private val router: Router
) : BaseCardsViewModel() {

    override val defaultTitle: String = "Обновления в избранном"

    override val loadOnCreate: Boolean = false

    override fun onCreate() {
        super.onCreate()

        viewModelScope.launch {
            if (authStateHolder.get() == AuthState.AUTH) {
                onRefreshClick()
            }
        }
    }

    override fun onColdCreate() {
        super.onColdCreate()
        authStateHolder
            .observe()
            .distinctUntilChanged()
            .drop(1)
            .onEach {
                if (it == AuthState.AUTH) {
                    onRefreshClick()
                }
            }
            .launchIn(viewModelScope)
    }

    override suspend fun getCoLoader(requestPage: Int): List<LibriaCard> = favoriteRepository
        .getFavorites(requestPage)
        .let { favoriteItems ->
            favoriteItems.items.sortedByDescending { it.torrentUpdate }.map { converter.toCard(it) }
        }

    override fun onLibriaCardClick(card: LibriaCard) {
        router.navigateTo(DetailsScreen(ReleaseId(card.id)))
    }
}