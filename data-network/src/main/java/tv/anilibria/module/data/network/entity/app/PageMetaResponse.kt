package tv.anilibria.module.data.network.entity.app

/**
 * Created by radiationx on 04.12.2017.
 */
data class PageMetaResponse(
    val page: Int?,
    var allPages: Int?,
    var perPage: Int?,
    var allItems: Int?,
) {
    fun isEnd(): Boolean {
        return page ?: 0 >= allPages ?: 0
    }
}
