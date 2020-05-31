package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.entity.user.User

interface UserApiDataSource {
    fun getSelf(): Single<User>
}