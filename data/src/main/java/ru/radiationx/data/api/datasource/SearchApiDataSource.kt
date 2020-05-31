package ru.radiationx.data.api.datasource

import io.reactivex.Single
import anilibria.tv.domain.entity.pagination.Paginated
import anilibria.tv.domain.entity.release.Release
import ru.radiationx.data.entity.app.search.SearchForm

interface SearchApiDataSource {
    fun getYears(): Single<List<String>>
    fun getGenres(): Single<List<String>>
    fun getSuggestions(name: String): Single<List<Release>>
    fun getMatches(form: SearchForm, page: Int): Single<Paginated<Release>>
}