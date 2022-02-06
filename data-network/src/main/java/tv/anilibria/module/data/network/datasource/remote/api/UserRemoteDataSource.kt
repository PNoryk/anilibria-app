package tv.anilibria.module.data.network.datasource.remote.api

import io.reactivex.Single
import tv.anilibria.module.domain.entity.other.User

interface UserRemoteDataSource {
    fun loadUser(): Single<User>
}