package ru.radiationx.anilibria.screen.search.genre

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.search.BaseSearchValuesViewModel
import ru.radiationx.anilibria.screen.search.SearchController
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.repos.SearchRepository
import tv.anilibria.feature.domain.entity.ReleaseGenre

@InjectConstructor
class SearchGenreViewModel(
    private val searchRepository: SearchRepository,
    private val searchController: SearchController,
    private val guidedRouter: GuidedRouter
) : BaseSearchValuesViewModel() {

    private val currentGenres = mutableListOf<ReleaseGenre>()

    override fun onColdCreate() {
        super.onColdCreate()
        searchRepository
            .observeGenres()
            .onEach {
                currentGenres.clear()
                currentGenres.addAll(it)
                currentValues.clear()
                currentValues.addAll(it.map { it.value })
                valuesData.value = it.map { it.value }
                progressState.value = false
                updateChecked()
                updateSelected()
            }
            .launchIn(viewModelScope)
    }

    override fun onCreate() {
        super.onCreate()
        progressState.value = true
        viewModelScope.launch {
            runCatching { searchRepository.getGenres() }
            progressState.value = false
        }
    }

    override fun applyValues() {
        viewModelScope.launch {
            searchController.genresEvent.emit(currentGenres.filterIndexed { index, item ->
                checkedValues.contains(
                    item.value
                )
            })
            guidedRouter.close()
        }
    }
}