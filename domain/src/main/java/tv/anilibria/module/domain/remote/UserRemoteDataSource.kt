package tv.anilibria.module.domain.remote

import io.reactivex.Single
import tv.anilibria.module.domain.entity.other.User

interface UserRemoteDataSource {
    fun loadUser(): Single<User>
}