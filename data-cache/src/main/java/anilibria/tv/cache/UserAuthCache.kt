package anilibria.tv.cache

import anilibria.tv.domain.entity.auth.UserAuth
import anilibria.tv.domain.entity.user.User
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface UserAuthCache {

    fun observeUser(): Observable<UserAuth>

    fun getUser(): Single<UserAuth>

    fun putUser(user: User): Completable

    fun deleteUser(): Completable
}