package anilibria.tv.cache

import anilibria.tv.domain.entity.menu.LinkMenu
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface LinkMenuCache {

    fun observeList(): Observable<List<LinkMenu>>

    fun getList(): Single<List<LinkMenu>>

    fun putList(items: List<LinkMenu>): Completable
}