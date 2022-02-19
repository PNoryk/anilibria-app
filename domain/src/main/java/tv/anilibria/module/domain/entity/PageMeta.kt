package tv.anilibria.module.domain.entity

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
