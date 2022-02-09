package tv.anilibria.module.data.local.holders

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.domain.entity.other.LinkMenuItem

interface LinkMenuLocalDataSource {
    fun observe(): Observable<List<LinkMenuItem>>
    fun get(): Single<List<LinkMenuItem>>
    fun put(data: List<LinkMenuItem>): Completable
}