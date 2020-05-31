package anilibria.tv.api.impl.datasource

import io.reactivex.Single
import anilibria.tv.domain.entity.comments.CommentsInfo
import anilibria.tv.api.impl.common.handleApiResponse
import anilibria.tv.api.impl.converter.CommentsConverter
import anilibria.tv.api.CommentsApiDataSource
import anilibria.tv.api.impl.service.CommentsService
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