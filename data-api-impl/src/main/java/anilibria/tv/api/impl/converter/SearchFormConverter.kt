package anilibria.tv.api.impl.converter

import anilibria.tv.api.impl.entity.search.SearchFormRequest
import anilibria.tv.domain.entity.search.SearchForm
import toothpick.InjectConstructor

@InjectConstructor
class SearchFormConverter {

    fun toRequest(source: SearchForm) = SearchFormRequest(
        source.years?.joinToString(",").orEmpty(),
        source.seasons?.joinToString(",").orEmpty(),
        source.genres?.joinToString(",").orEmpty(),
        when (source.sort) {
            SearchForm.Sort.RATING -> "2"
            SearchForm.Sort.DATE -> "1"
        },
        if (source.onlyCompleted) "2" else "1"
    )
}