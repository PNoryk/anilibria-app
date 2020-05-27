package ru.radiationx.data.api.service

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.entity.user.UserResponse
import ru.radiationx.data.api.entity.common.ApiBaseResponse

interface UserApi {

    fun getSelf(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<UserResponse>>
}