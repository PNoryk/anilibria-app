package ru.radiationx.anilibria.screen.watching

import io.reactivex.Single
import ru.radiationx.anilibria.common.BaseCardsViewModel
import ru.radiationx.anilibria.common.CardsDataConverter
import ru.radiationx.anilibria.common.LibriaCard
import ru.radiationx.anilibria.screen.DetailsScreen
import ru.radiationx.data.entity.common.AuthState
import ru.radiationx.data.interactors.ReleaseInteractor
import ru.radiationx.data.repository.AuthRepository
import ru.radiationx.data.repository.FavoriteRepository
import ru.radiationx.data.repository.ReleaseRepository
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor

@InjectConstructor
class WatchingFavoritesViewModel(
    private val favoriteRepository: FavoriteRepository,
    private val releaseInteractor: ReleaseInteractor,
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

    override fun getLoader(requestPage: Int): Single<List<LibriaCard>> = favoriteRepository
        .getFavorites(requestPage)
        .map { favoriteItems ->
            favoriteItems.data.map { converter.toCard(it) }
        }

    override fun onLibriaCardClick(card: LibriaCard) {
        router.navigateTo(DetailsScreen(card.id))
    }
}