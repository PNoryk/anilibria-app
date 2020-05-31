package anilibria.tv.api.impl.service

import io.reactivex.Single
import retrofit2.http.FieldMap
import anilibria.tv.api.impl.entity.common.ApiBaseResponse
import anilibria.tv.api.impl.entity.user.UserResponse

interface UserService {

    fun getSelf(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<UserResponse>>
}