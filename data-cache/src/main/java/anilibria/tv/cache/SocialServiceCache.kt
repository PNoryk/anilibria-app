package anilibria.tv.cache

import anilibria.tv.domain.entity.auth.SocialService
import anilibria.tv.domain.entity.menu.LinkMenu
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface SocialServiceCache {

    fun observeList(): Observable<List<SocialService>>

    fun getList(): Single<List<SocialService>>

    fun putList(items: List<SocialService>): Completable

    fun clear(): Completable
}