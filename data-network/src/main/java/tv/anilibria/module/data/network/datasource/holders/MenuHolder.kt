package tv.anilibria.module.data.network.datasource.holders

import io.reactivex.Observable
import tv.anilibria.module.data.network.entity.app.other.LinkMenuItemResponse

interface MenuHolder {
    fun observe(): Observable<List<LinkMenuItemResponse>>
    fun save(items: List<LinkMenuItemResponse>)
    fun get(): List<LinkMenuItemResponse>
}