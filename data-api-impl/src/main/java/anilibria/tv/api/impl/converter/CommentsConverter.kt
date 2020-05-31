package anilibria.tv.api.impl.converter

import anilibria.tv.domain.entity.comments.CommentsInfo
import anilibria.tv.api.impl.entity.comments.CommentsInfoResponse
import toothpick.InjectConstructor

@InjectConstructor
class CommentsConverter {

    fun toDomain(response: CommentsInfoResponse) = CommentsInfo(
        baseUrl = response.baseUrl,
        script = response.script
    )
}