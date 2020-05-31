package anilibria.tv.api

import io.reactivex.Single
import anilibria.tv.domain.entity.comments.CommentsInfo

interface CommentsApiDataSource {
    fun getComments(): Single<CommentsInfo>
}