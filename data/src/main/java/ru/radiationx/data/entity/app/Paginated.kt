package ru.radiationx.data.entity.app

/**
 * Created by radiationx on 04.12.2017.
 */
@Deprecated("old data")
class Paginated<out T>(val data: T) {
    var page: Int = 1
    var allPages = 1
    var perPage = 10
    var allItems = 0

    fun isEnd(): Boolean {
        return page >= allPages
    }
}
