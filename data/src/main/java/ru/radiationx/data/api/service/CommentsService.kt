package ru.radiationx.data.api.service

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.entity.comments.CommentsInfoResponse
import ru.radiationx.data.api.entity.common.ApiBaseResponse

interface CommentsService {

    fun getComments(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<CommentsInfoResponse>>
}