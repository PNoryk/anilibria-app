package anilibria.tv.api.impl.entity.search

import anilibria.tv.domain.entity.search.SearchForm

data class SearchFormRequest(
    val years: String,
    val seasons: String,
    val genres: String,
    val sort: String,
    val onlyCompleted: String
)