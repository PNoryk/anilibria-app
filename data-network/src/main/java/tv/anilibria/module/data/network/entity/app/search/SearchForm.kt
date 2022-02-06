package tv.anilibria.module.data.network.entity.app.search

import tv.anilibria.module.data.network.datasource.storage.GenresStorage
import tv.anilibria.module.data.network.entity.app.release.GenreItem
import tv.anilibria.module.data.network.entity.app.release.SeasonItem
import tv.anilibria.module.data.network.entity.app.release.YearItem

data class SearchForm(
    val years: List<YearItem>? = null,
    val seasons: List<SeasonItem>? = null,
    val genres: List<GenreItem>? = null,
    val sort: Sort = Sort.RATING,
    val onlyCompleted: Boolean = false
) {
    enum class Sort {
        RATING, DATE
    }
}