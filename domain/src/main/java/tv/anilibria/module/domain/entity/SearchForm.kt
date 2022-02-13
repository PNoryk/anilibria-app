package tv.anilibria.module.domain.entity

data class SearchForm(
    val years: List<String>? = null,
    val seasons: List<String>? = null,
    val genres: List<String>? = null,
    val sort: Sort = Sort.RATING,
    val onlyCompleted: Boolean = false
) {
    enum class Sort {
        RATING, DATE
    }
}