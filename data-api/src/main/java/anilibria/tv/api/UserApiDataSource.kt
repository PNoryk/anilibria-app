package anilibria.tv.api

import io.reactivex.Single
import anilibria.tv.domain.entity.user.User

interface UserApiDataSource {
    fun getSelf(): Single<User>
}