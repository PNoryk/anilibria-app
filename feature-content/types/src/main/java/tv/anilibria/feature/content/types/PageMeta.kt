package tv.anilibria.feature.content.types

/**
 * Created by radiationx on 04.12.2017.
 */
data class PageMeta(
    val page: Int?,
    val perPage: Int?,
    val allPages: Int?,
    val allItems: Int?,
) {
    fun isEnd(): Boolean {
        return page ?: 0 >= allPages ?: 0
    }
}
