package ru.radiationx.anilibria.screen.search.season

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.search.BaseSearchValuesViewModel
import ru.radiationx.anilibria.screen.search.SearchController
import toothpick.InjectConstructor
import tv.anilibria.module.data.repos.SearchRepository
import tv.anilibria.module.domain.entity.ReleaseSeason

@InjectConstructor
class SearchSeasonViewModel(
    private val searchRepository: SearchRepository,
    private val searchController: SearchController,
    private val guidedRouter: GuidedRouter
) : BaseSearchValuesViewModel() {

    private val currentSeasons = mutableListOf<ReleaseSeason>()

    override fun onCreate() {
        super.onCreate()
        viewModelScope.launch {
            runCatching {
                searchRepository.getSeasons()
            }.onSuccess {
                currentSeasons.clear()
                currentSeasons.addAll(it)
                currentValues.clear()
                currentValues.addAll(it.map { it.value })
                valuesData.value = it.map { it.value }
                updateChecked()
                updateSelected()
            }
        }
    }

    override fun applyValues() {
        searchController.seasonsEvent.accept(currentSeasons.filterIndexed { index, item ->
            checkedValues.contains(
                item.value
            )
        })
        guidedRouter.close()
    }
}