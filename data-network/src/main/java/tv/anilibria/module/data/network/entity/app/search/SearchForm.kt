package tv.anilibria.module.data.network.entity.app.search

import tv.anilibria.module.data.network.entity.app.release.GenreItemResponse
import tv.anilibria.module.data.network.entity.app.release.SeasonItem
import tv.anilibria.module.data.network.entity.app.release.YearItemResponse

data class SearchForm(
    val years: List<YearItemResponse>? = null,
    val seasons: List<SeasonItem>? = null,
    val genres: List<GenreItemResponse>? = null,
    val sort: Sort = Sort.RATING,
    val onlyCompleted: Boolean = false
) {
    enum class Sort {
        RATING, DATE
    }
}