package tv.anilibria.module.data.local.holders

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.local.DataWrapper
import tv.anilibria.module.domain.entity.other.User

/**
 * Created by radiationx on 11.01.18.
 */
interface UserLocalDataSource {
    fun observe(): Observable<DataWrapper<User>>
    fun get(): Single<DataWrapper<User>>
    fun put(data: User): Completable
    fun delete(): Completable
}