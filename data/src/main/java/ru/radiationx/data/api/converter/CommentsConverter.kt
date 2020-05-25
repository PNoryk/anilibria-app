package ru.radiationx.data.api.converter

import ru.radiationx.data.adomain.CommentsInfo
import ru.radiationx.data.api.remote.CommentsInfoResponse
import toothpick.InjectConstructor

@InjectConstructor
class CommentsConverter {

    fun toDomain(response: CommentsInfoResponse) = CommentsInfo(
        baseUrl = response.baseUrl,
        script = response.script
    )
}