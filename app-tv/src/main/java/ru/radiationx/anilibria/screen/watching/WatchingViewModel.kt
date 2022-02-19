package ru.radiationx.anilibria.screen.watching

import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.radiationx.anilibria.common.BaseRowsViewModel
import ru.radiationx.data.entity.common.AuthState
import ru.radiationx.data.repository.AuthRepository
import toothpick.InjectConstructor
import tv.anilibria.module.data.repos.EpisodeHistoryRepository
import tv.anilibria.module.data.repos.HistoryRepository

@InjectConstructor
class WatchingViewModel(
    private val authRepository: AuthRepository,
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

        authRepository
            .observeUser()
            .observeOn(AndroidSchedulers.mainThread())
            .lifeSubscribe {
                updateAvailableRow(FAVORITES_ROW_ID, it.authState == AuthState.AUTH)
            }
    }
}