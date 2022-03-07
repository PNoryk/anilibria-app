package tv.anilibria.feature.content.types

data class SearchForm(
    val years: List<ReleaseYear>? = null,
    val seasons: List<ReleaseSeason>? = null,
    val genres: List<ReleaseGenre>? = null,
    val sort: Sort = Sort.RATING,
    val onlyCompleted: Boolean = false
) {
    enum class Sort {
        RATING, DATE
    }
}