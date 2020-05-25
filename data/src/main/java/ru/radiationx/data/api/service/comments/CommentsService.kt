package ru.radiationx.data.api.service.comments

import io.reactivex.Single
import ru.radiationx.data.adomain.comments.CommentsInfo
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.CommentsConverter
import toothpick.InjectConstructor

@InjectConstructor
class CommentsService(
    private val commentsApi: CommentsApi,
    private val commentsConverter: CommentsConverter
) {

    fun getComments(): Single<CommentsInfo> = commentsApi
        .getComments(mapOf("query" to "vkcomments"))
        .handleApiResponse()
        .map { commentsConverter.toDomain(it) }
}