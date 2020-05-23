package ru.radiationx.data.api.service.comments

import io.reactivex.Single
import ru.radiationx.data.api.remote.CommentsInfoResponse
import ru.radiationx.data.api.remote.common.ApiBaseResponse

interface CommentsApi {

    fun getComments(): Single<ApiBaseResponse<CommentsInfoResponse>>
}