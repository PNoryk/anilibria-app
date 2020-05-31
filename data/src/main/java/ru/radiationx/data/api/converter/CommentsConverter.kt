package ru.radiationx.data.api.converter

import anilibria.tv.domain.entity.comments.CommentsInfo
import ru.radiationx.data.api.entity.comments.CommentsInfoResponse
import toothpick.InjectConstructor

@InjectConstructor
class CommentsConverter {

    fun toDomain(response: CommentsInfoResponse) = CommentsInfo(
        baseUrl = response.baseUrl,
        script = response.script
    )
}