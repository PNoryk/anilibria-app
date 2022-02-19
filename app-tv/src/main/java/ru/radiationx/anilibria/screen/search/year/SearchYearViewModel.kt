package ru.radiationx.anilibria.screen.search.year

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.search.BaseSearchValuesViewModel
import ru.radiationx.anilibria.screen.search.SearchController
import ru.radiationx.data.entity.app.release.YearItem
import toothpick.InjectConstructor
import tv.anilibria.module.data.repos.SearchRepository
import tv.anilibria.module.domain.entity.ReleaseYear

@InjectConstructor
class SearchYearViewModel(
    private val searchRepository: SearchRepository,
    private val searchController: SearchController,
    private val guidedRouter: GuidedRouter
) : BaseSearchValuesViewModel() {

    private val currentYears = mutableListOf<ReleaseYear>()

    override fun onColdCreate() {
        super.onColdCreate()
        searchRepository
            .observeYears()
            .onEach {
                currentYears.clear()
                currentYears.addAll(it)
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
            runCatching { searchRepository.getYears() }
            progressState.value = false
        }
    }

    override fun applyValues() {
        searchController.yearsEvent.accept(currentYears.filterIndexed { index, item ->
            checkedValues.contains(
                item.value
            )
        })
        guidedRouter.close()
    }
}