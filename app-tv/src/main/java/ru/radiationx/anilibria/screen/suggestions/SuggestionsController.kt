package ru.radiationx.anilibria.screen.suggestions

import com.jakewharton.rxrelay2.PublishRelay
import toothpick.InjectConstructor
import tv.anilibria.module.domain.entity.release.Release

@InjectConstructor
class SuggestionsController {

    val resultEvent = PublishRelay.create<SearchResult>()

    data class SearchResult(
        val items: List<Release>,
        val query: String,
        val validQuery: Boolean
    )
}