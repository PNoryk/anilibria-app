package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.entity.comments.CommentsInfo

interface CommentsApiDataSource {
    fun getComments(): Single<CommentsInfo>
}