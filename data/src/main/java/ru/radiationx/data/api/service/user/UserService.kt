package ru.radiationx.data.api.service.user

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.remote.UserResponse
import ru.radiationx.data.api.remote.common.ApiBaseResponse
import ru.radiationx.data.api.remote.common.handleApiResponse

class UserService(
    private val userApi: UserApi
) {

    fun getSelf(): Single<UserResponse> = userApi
        .getSelf(mapOf("query" to "user"))
        .handleApiResponse()
}