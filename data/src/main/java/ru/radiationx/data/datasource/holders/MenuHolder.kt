package ru.radiationx.data.datasource.holders

import io.reactivex.Observable
import ru.radiationx.data.entity.app.other.LinkMenuItem

@Deprecated("old data")
interface MenuHolder {
    fun observe(): Observable<List<LinkMenuItem>>
    fun save(items: List<LinkMenuItem>)
    fun get(): List<LinkMenuItem>
}