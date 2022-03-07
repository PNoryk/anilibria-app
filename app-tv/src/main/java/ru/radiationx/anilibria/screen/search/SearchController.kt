package ru.radiationx.anilibria.screen.search

import kotlinx.coroutines.flow.MutableSharedFlow
import toothpick.InjectConstructor
import tv.anilibria.feature.content.types.ReleaseGenre
import tv.anilibria.feature.content.types.ReleaseSeason
import tv.anilibria.feature.content.types.ReleaseYear
import tv.anilibria.feature.content.types.SearchForm

@InjectConstructor
class SearchController {

    val yearsEvent = MutableSharedFlow<List<ReleaseYear>>()
    val seasonsEvent = MutableSharedFlow<List<ReleaseSeason>>()
    val genresEvent = MutableSharedFlow<List<ReleaseGenre>>()
    val sortEvent = MutableSharedFlow<SearchForm.Sort>()
    val completedEvent = MutableSharedFlow<Boolean>()

    val applyFormEvent = MutableSharedFlow<SearchForm>()
}