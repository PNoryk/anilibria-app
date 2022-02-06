package tv.anilibria.module.data.network.entity.app

/**
 * Created by radiationx on 04.12.2017.
 */
class PaginatedResponse<out T>(val data: T) {
    var page: Int = 1
    var allPages = 1
    var perPage = 10
    var allItems = 0

    fun isEnd(): Boolean {
        return page >= allPages
    }
}
