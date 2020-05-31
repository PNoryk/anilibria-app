package anilibria.tv.api.impl.service

import io.reactivex.Single
import retrofit2.http.FieldMap
import anilibria.tv.api.impl.entity.comments.CommentsInfoResponse
import anilibria.tv.api.impl.entity.common.ApiBaseResponse

interface CommentsService {

    fun getComments(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<CommentsInfoResponse>>
}