package ru.radiationx.anilibria.screen.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
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

        searchController.yearsEvent.lifeSubscribe {
            searchForm = searchForm.copy(years = it)
            updateDataByForm()
        }
        searchController.seasonsEvent.lifeSubscribe {
            searchForm = searchForm.copy(seasons = it)
            updateDataByForm()
        }
        searchController.genresEvent.lifeSubscribe {
            searchForm = searchForm.copy(genres = it)
            updateDataByForm()
        }
        searchController.sortEvent.lifeSubscribe {
            searchForm = searchForm.copy(sort = it)
            updateDataByForm()
        }
        searchController.completedEvent.lifeSubscribe {
            searchForm = searchForm.copy(onlyCompleted = it)
            updateDataByForm()
        }
    }

    fun onYearClick() {
        guidedRouter.open(SearchYearGuidedScreen(searchForm.years))
    }

    fun onSeasonClick() {
        guidedRouter.open(SearchSeasonGuidedScreen(searchForm.seasons))
    }

    fun onGenreClick() {
        guidedRouter.open(SearchGenreGuidedScreen(searchForm.genres))
    }

    fun onSortClick() {
        guidedRouter.open(SearchSortGuidedScreen(searchForm.sort))
    }

    fun onOnlyCompletedClick() {
        guidedRouter.open(SearchCompletedGuidedScreen(searchForm.onlyCompleted))
    }

    private fun updateDataByForm() {
        Log.e("kokoko", "updateDataByForm $searchForm")
        yearData.value = searchForm.years?.generateListTitle("Все годы")
        seasonData.value = searchForm.seasons?.generateListTitle("Все сезоны")
        genreData.value = searchForm.genres?.generateListTitle("Все жанры")
        sortData.value = when (searchForm.sort) {
            SearchForm.Sort.RATING -> "По популярности"
            SearchForm.Sort.DATE -> "По новизне"
        }
        onlyCompletedData.value = if (searchForm.onlyCompleted) {
            "Только завершенные"
        } else {
            "Все"
        }

        searchController.applyFormEvent.accept(searchForm)
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