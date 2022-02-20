package ru.radiationx.anilibria.screen.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.*
import toothpick.InjectConstructor
import tv.anilibria.module.domain.entity.SearchForm

@InjectConstructor
class SearchFormViewModel(
    private val searchController: SearchController,
    private val guidedRouter: GuidedRouter
) : LifecycleViewModel() {

    val yearData = MutableLiveData<String>()
    val seasonData = MutableLiveData<String>()
    val genreData = MutableLiveData<String>()
    val sortData = MutableLiveData<String>()
    val onlyCompletedData = MutableLiveData<String>()

    private var searchForm = SearchForm()

    override fun onColdCreate() {
        super.onColdCreate()

        updateDataByForm()

        searchController.yearsEvent.onEach {
            searchForm = searchForm.copy(years = it)
            updateDataByForm()
        }
        searchController.seasonsEvent.onEach {
            searchForm = searchForm.copy(seasons = it)
            updateDataByForm()
        }
        searchController.genresEvent.onEach {
            searchForm = searchForm.copy(genres = it)
            updateDataByForm()
        }
        searchController.sortEvent.onEach {
            searchForm = searchForm.copy(sort = it)
            updateDataByForm()
        }
        searchController.completedEvent.onEach {
            searchForm = searchForm.copy(onlyCompleted = it)
            updateDataByForm()
        }
    }

    fun onYearClick() {
        guidedRouter.open(SearchYearGuidedScreen(searchForm.years.orEmpty()))
    }

    fun onSeasonClick() {
        guidedRouter.open(SearchSeasonGuidedScreen(searchForm.seasons.orEmpty()))
    }

    fun onGenreClick() {
        guidedRouter.open(SearchGenreGuidedScreen(searchForm.genres.orEmpty()))
    }

    fun onSortClick() {
        guidedRouter.open(SearchSortGuidedScreen(searchForm.sort))
    }

    fun onOnlyCompletedClick() {
        guidedRouter.open(SearchCompletedGuidedScreen(searchForm.onlyCompleted))
    }

    private fun updateDataByForm() {
        viewModelScope.launch {
            Log.e("kokoko", "updateDataByForm $searchForm")
            yearData.value = searchForm.years?.map { it.value }.generateListTitle("Все годы")
            seasonData.value = searchForm.seasons?.map { it.value }.generateListTitle("Все сезоны")
            genreData.value = searchForm.genres?.map { it.value }.generateListTitle("Все жанры")
            sortData.value = when (searchForm.sort) {
                SearchForm.Sort.RATING -> "По популярности"
                SearchForm.Sort.DATE -> "По новизне"
            }
            onlyCompletedData.value = if (searchForm.onlyCompleted) {
                "Только завершенные"
            } else {
                "Все"
            }

            searchController.applyFormEvent.emit(searchForm)
        }
    }

    private fun List<String>?.generateListTitle(fallback: String, take: Int = 2): String {
        if (this == null || isEmpty()) {
            return fallback
        }
        var result = take(take).joinToString()
        if (size > take) {
            result += "… +${size - take}"
        }
        return result
    }
}