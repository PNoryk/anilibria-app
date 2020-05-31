package anilibria.tv.api

import io.reactivex.Single
import anilibria.tv.domain.entity.pagination.Paginated
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.search.SearchForm

interface SearchApiDataSource {
    fun getYears(): Single<List<String>>
    fun getGenres(): Single<List<String>>
    fun getSuggestions(name: String): Single<List<Release>>
    fun getMatches(form: SearchForm, page: Int): Single<Paginated<Release>>
}