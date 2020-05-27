package ru.radiationx.data.api.service.comments

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.entity.comments.CommentsInfoResponse
import ru.radiationx.data.api.entity.common.ApiBaseResponse

interface CommentsApi {

    fun getComments(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<CommentsInfoResponse>>
}