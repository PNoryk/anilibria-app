package ru.radiationx.anilibria.presentation.search

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.radiationx.anilibria.ui.fragments.search.SearchScreenState
import tv.anilibria.feature.content.types.ReleaseGenre
import tv.anilibria.feature.content.types.ReleaseSeason
import tv.anilibria.feature.content.types.ReleaseYear
import tv.anilibria.feature.content.types.SearchForm

@StateStrategyType(AddToEndSingleStrategy::class)
interface SearchCatalogView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showState(state: SearchScreenState)

    fun updateInfo(sort: SearchForm.Sort, filters: Int)

    fun showGenres(genres: List<ReleaseGenre>)
    fun showYears(years: List<ReleaseYear>)
    fun showSeasons(seasons: List<ReleaseSeason>)
    fun selectGenres(genres: List<ReleaseGenre>)
    fun selectYears(years: List<ReleaseYear>)
    fun selectSeasons(seasons: List<ReleaseSeason>)
    fun setSorting(sorting: SearchForm.Sort)
    fun setComplete(complete: Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun showDialog()
}
