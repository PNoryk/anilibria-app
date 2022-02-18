package ru.radiationx.data.entity.app.search

import ru.radiationx.data.datasource.storage.GenresStorage
import ru.radiationx.data.entity.app.release.GenreItem
import ru.radiationx.data.entity.app.release.SeasonItem
import ru.radiationx.data.entity.app.release.YearItem

@Deprecated("old data")
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