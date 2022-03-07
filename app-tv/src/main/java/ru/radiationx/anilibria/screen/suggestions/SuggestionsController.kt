package ru.radiationx.anilibria.screen.suggestions

import kotlinx.coroutines.flow.MutableSharedFlow
import toothpick.InjectConstructor
import tv.anilibria.feature.content.types.release.Release

@InjectConstructor
class SuggestionsController {

    val resultEvent = MutableSharedFlow<SearchResult>()

    data class SearchResult(
        val items: List<Release>,
        val query: String,
        val validQuery: Boolean
    )
}