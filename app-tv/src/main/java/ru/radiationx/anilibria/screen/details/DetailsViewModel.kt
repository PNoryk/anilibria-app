package ru.radiationx.anilibria.screen.details

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.BaseRowsViewModel
import toothpick.InjectConstructor
import tv.anilibria.feature.auth.data.AuthStateHolder
import tv.anilibria.feature.content.data.repos.ReleaseCacheRepository
import tv.anilibria.feature.content.data.repos.HistoryRepository
import tv.anilibria.feature.content.data.repos.ReleaseRepository
import tv.anilibria.feature.content.types.release.ReleaseCode
import tv.anilibria.feature.content.types.release.ReleaseId

@InjectConstructor
class DetailsViewModel(
    private val releaseCacheRepository: ReleaseCacheRepository,
    private val releaseRepository: ReleaseRepository,
    private val historyRepository: HistoryRepository,
    private val authStateHolder: AuthStateHolder
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

    lateinit var releaseId: ReleaseId

    override val rowIds: List<Long> = listOf(RELEASE_ROW_ID, RELATED_ROW_ID, RECOMMENDS_ROW_ID)

    override val availableRows: MutableSet<Long> =
        mutableSetOf(RELEASE_ROW_ID, RELATED_ROW_ID, RECOMMENDS_ROW_ID)

    override fun onCreate() {
        super.onCreate()

        observeRelease()
        loadRelease()

        authStateHolder
            .observe()
            .distinctUntilChanged()
            .drop(1)
            .onEach {
                loadRelease()
            }
            .launchIn(viewModelScope)
    }

    private fun observeRelease() {
        releaseCacheRepository
            .observeRelease(releaseId, null)
            .filterNotNull()
            .map { getReleasesFromDesc(it.description?.text.orEmpty()) }
            .onEach { updateAvailableRow(RELATED_ROW_ID, it.isNotEmpty()) }
            .launchIn(viewModelScope)
    }

    private fun loadRelease() {
        viewModelScope.launch {
            runCatching {
                releaseRepository.getRelease(releaseId)
            }.onSuccess {
                historyRepository.putRelease(it.id)
            }
        }
    }
}