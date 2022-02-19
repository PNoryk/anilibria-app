package ru.radiationx.anilibria.screen.details

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import ru.radiationx.anilibria.common.BaseRowsViewModel
import ru.radiationx.data.repository.AuthRepository
import toothpick.InjectConstructor
import tv.anilibria.module.data.ReleaseInteractor
import tv.anilibria.module.data.repos.HistoryRepository
import tv.anilibria.module.domain.entity.release.ReleaseCode
import tv.anilibria.module.domain.entity.release.ReleaseId

@InjectConstructor
class DetailsViewModel(
    private val releaseInteractor: ReleaseInteractor,
    private val historyRepository: HistoryRepository,
    private val authRepository: AuthRepository
) : BaseRowsViewModel() {

    companion object {
        private val linkPattern = Regex("\\/release\\/([^.]+?)\\.html")

        const val RELEASE_ROW_ID = 1L
        const val RELATED_ROW_ID = 2L
        const val RECOMMENDS_ROW_ID = 3L

        fun getReleasesFromDesc(description: String): List<ReleaseCode> {
            return linkPattern.findAll(description).map { ReleaseCode(it.groupValues[1]) }.toList()
        }
    }

    var releaseId: ReleaseId? = null

    override val rowIds: List<Long> = listOf(RELEASE_ROW_ID, RELATED_ROW_ID, RECOMMENDS_ROW_ID)

    override val availableRows: MutableSet<Long> =
        mutableSetOf(RELEASE_ROW_ID, RELATED_ROW_ID, RECOMMENDS_ROW_ID)

    override fun onCreate() {
        super.onCreate()

        loadRelease()

        authRepository
            .observeUser()
            .map { it.authState }
            .distinctUntilChanged()
            .skip(1)
            .lifeSubscribe {
                loadRelease()
            }

        (releaseInteractor.getFull(releaseId) ?: releaseInteractor.getItem(releaseId))?.also {
            val releases = getReleasesFromDesc(it.description?.text.orEmpty())
            updateAvailableRow(RELATED_ROW_ID, releases.isNotEmpty())
        }

        releaseInteractor
            .observeFull(releaseId)
            .map { getReleasesFromDesc(it.description?.text.orEmpty()) }
            .onEach { updateAvailableRow(RELATED_ROW_ID, it.isNotEmpty()) }
            .launchIn(viewModelScope)
    }

    private fun loadRelease() {
        releaseInteractor
            .loadRelease(releaseId)
            .take(1)
            .onEach { historyRepository.putRelease(it.id) }
            .launchIn(viewModelScope)
    }
}