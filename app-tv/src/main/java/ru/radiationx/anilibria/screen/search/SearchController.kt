package ru.radiationx.anilibria.screen.search

import com.jakewharton.rxrelay2.PublishRelay
import toothpick.InjectConstructor
import tv.anilibria.module.domain.entity.ReleaseGenre
import tv.anilibria.module.domain.entity.ReleaseSeason
import tv.anilibria.module.domain.entity.ReleaseYear
import tv.anilibria.module.domain.entity.SearchForm

@InjectConstructor
class SearchController {

    val yearsEvent = PublishRelay.create<List<ReleaseYear>>()
    val seasonsEvent = PublishRelay.create<List<ReleaseSeason>>()
    val genresEvent = PublishRelay.create<List<ReleaseGenre>>()
    val sortEvent = PublishRelay.create<SearchForm.Sort>()
    val completedEvent = PublishRelay.create<Boolean>()

    val applyFormEvent = PublishRelay.create<SearchForm>()
}