package anilibria.tv.storage

import anilibria.tv.domain.entity.auth.UserAuth
import anilibria.tv.domain.entity.user.User
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface UserAuthStorageDataSource {

    fun getUser(): Single<UserAuth>

    fun putUser(user: UserAuth): Completable
}