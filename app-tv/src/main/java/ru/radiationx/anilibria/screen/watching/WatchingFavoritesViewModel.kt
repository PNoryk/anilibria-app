package ru.radiationx.anilibria.screen.watching

import ru.radiationx.anilibria.common.BaseCardsViewModel
import ru.radiationx.anilibria.common.CardsDataConverter
import ru.radiationx.anilibria.common.LibriaCard
import ru.radiationx.anilibria.screen.DetailsScreen
import ru.radiationx.data.entity.common.AuthState
import ru.radiationx.data.repository.AuthRepository
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.module.data.repos.FavoriteRepository

@InjectConstructor
class WatchingFavoritesViewModel(
    private val favoriteRepository: FavoriteRepository,
    private val authRepository: AuthRepository,
    private val converter: CardsDataConverter,
    private val router: Router
) : BaseCardsViewModel() {

    override val defaultTitle: String = "Избранное"

    override val loadOnCreate: Boolean = false

    override fun onCreate() {
        super.onCreate()
        if (authRepository.getAuthState() == AuthState.AUTH) {
            onRefreshClick()
        }
    }

    override fun onColdCreate() {
        super.onColdCreate()
        authRepository
            .observeUser()
            .map { it.authState }
            .distinctUntilChanged()
            .skip(1)
            .lifeSubscribe {
                if (it == AuthState.AUTH) {
                    onRefreshClick()
                }
            }
    }

    override suspend fun getCoLoader(requestPage: Int): List<LibriaCard> = favoriteRepository
        .getFavorites(requestPage)
        .let { favoriteItems ->
            favoriteItems.items.map { converter.toCard(it) }
        }

    override fun onLibriaCardClick(card: LibriaCard) {
        router.navigateTo(DetailsScreen(card.id))
    }
}