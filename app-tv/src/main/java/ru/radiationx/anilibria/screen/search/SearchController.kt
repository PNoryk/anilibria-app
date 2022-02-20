package ru.radiationx.anilibria.screen.search

import kotlinx.coroutines.flow.MutableSharedFlow
import toothpick.InjectConstructor
import tv.anilibria.module.domain.entity.ReleaseGenre
import tv.anilibria.module.domain.entity.ReleaseSeason
import tv.anilibria.module.domain.entity.ReleaseYear
import tv.anilibria.module.domain.entity.SearchForm

@InjectConstructor
class SearchController {

    val yearsEvent = MutableSharedFlow<List<ReleaseYear>>()
    val seasonsEvent = MutableSharedFlow<List<ReleaseSeason>>()
    val genresEvent = MutableSharedFlow<List<ReleaseGenre>>()
    val sortEvent = MutableSharedFlow<SearchForm.Sort>()
    val completedEvent = MutableSharedFlow<Boolean>()

    val applyFormEvent = MutableSharedFlow<SearchForm>()
}