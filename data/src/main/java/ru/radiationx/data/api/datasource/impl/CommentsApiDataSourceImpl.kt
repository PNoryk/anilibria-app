package ru.radiationx.data.api.datasource.impl

import io.reactivex.Single
import ru.radiationx.data.adomain.entity.comments.CommentsInfo
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.CommentsConverter
import ru.radiationx.data.api.datasource.CommentsApiDataSource
import ru.radiationx.data.api.service.CommentsService
import toothpick.InjectConstructor

@InjectConstructor
class CommentsApiDataSourceImpl(
    private val commentsService: CommentsService,
    private val commentsConverter: CommentsConverter
) : CommentsApiDataSource {

    override fun getComments(): Single<CommentsInfo> = commentsService
        .getComments(mapOf("query" to "vkcomments"))
        .handleApiResponse()
        .map { commentsConverter.toDomain(it) }
}