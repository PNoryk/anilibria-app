package ru.radiationx.data.api.service.comments

import io.reactivex.Single
import ru.radiationx.data.api.remote.CommentsInfoResponse
import ru.radiationx.data.api.common.handleApiResponse

class CommentsService(
    private val commentsApi: CommentsApi
) {

    fun getComments(): Single<CommentsInfoResponse> = commentsApi
        .getComments(mapOf("query" to "vkcomments"))
        .handleApiResponse()
}