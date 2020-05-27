package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.comments.CommentsInfo
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.CommentsConverter
import ru.radiationx.data.api.service.CommentsService
import toothpick.InjectConstructor

@InjectConstructor
class CommentsDataSource(
    private val commentsService: CommentsService,
    private val commentsConverter: CommentsConverter
) {

    fun getComments(): Single<CommentsInfo> = commentsService
        .getComments(mapOf("query" to "vkcomments"))
        .handleApiResponse()
        .map { commentsConverter.toDomain(it) }
}