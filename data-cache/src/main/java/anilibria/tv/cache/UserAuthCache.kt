package anilibria.tv.cache

import anilibria.tv.domain.entity.auth.UserAuth
import anilibria.tv.domain.entity.user.User
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface UserAuthCache {

    fun observe(): Observable<UserAuth>

    fun get(): Single<UserAuth>

    fun save(user: User): Completable

    fun clear(): Completable
}