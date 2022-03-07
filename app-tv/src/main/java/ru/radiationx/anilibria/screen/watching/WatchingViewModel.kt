package ru.radiationx.anilibria.screen.watching

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.radiationx.anilibria.common.BaseRowsViewModel
import toothpick.InjectConstructor
import tv.anilibria.feature.auth.data.AuthStateHolder
import tv.anilibria.feature.auth.data.domain.AuthState
import tv.anilibria.feature.player.data.EpisodeHistoryRepository
import tv.anilibria.feature.data.repos.HistoryRepository

@InjectConstructor
class WatchingViewModel(
    private val authStateHolder: AuthStateHolder,
    private val historyRepository: HistoryRepository,
    private val episodesCheckerHolder: EpisodeHistoryRepository
) : BaseRowsViewModel() {

    companion object {
        const val HISTORY_ROW_ID = 1L
        const val CONTINUE_ROW_ID = 2L
        const val FAVORITES_ROW_ID = 3L
        const val RECOMMENDS_ROW_ID = 4L
    }

    override val rowIds: List<Long> =
        listOf(CONTINUE_ROW_ID, HISTORY_ROW_ID, FAVORITES_ROW_ID, RECOMMENDS_ROW_ID)

    override val availableRows: MutableSet<Long> =
        mutableSetOf(CONTINUE_ROW_ID, HISTORY_ROW_ID, RECOMMENDS_ROW_ID)

    override fun onCreate() {
        super.onCreate()

        episodesCheckerHolder
            .observeAll()
            .onEach {
                updateAvailableRow(CONTINUE_ROW_ID, it.isNotEmpty())
            }
            .launchIn(viewModelScope)

        historyRepository
            .observeReleases()
            .onEach {
                updateAvailableRow(HISTORY_ROW_ID, it.isNotEmpty())
            }
            .launchIn(viewModelScope)

        authStateHolder
            .observe()
            .distinctUntilChanged()
            .onEach {
                updateAvailableRow(FAVORITES_ROW_ID, it == AuthState.AUTH)
            }
            .launchIn(viewModelScope)
    }
}