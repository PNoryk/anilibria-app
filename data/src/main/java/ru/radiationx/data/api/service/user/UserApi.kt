package ru.radiationx.data.api.service.user

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.remote.user.UserResponse
import ru.radiationx.data.api.remote.common.ApiBaseResponse

interface UserApi {

    fun getSelf(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<UserResponse>>
}