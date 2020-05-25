package ru.radiationx.data.api.service.comments

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.remote.comments.CommentsInfoResponse
import ru.radiationx.data.api.remote.common.ApiBaseResponse

interface CommentsApi {

    fun getComments(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<CommentsInfoResponse>>
}