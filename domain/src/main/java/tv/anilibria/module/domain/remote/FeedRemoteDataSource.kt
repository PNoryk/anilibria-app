package tv.anilibria.module.domain.remote

import io.reactivex.Single
import tv.anilibria.module.domain.entity.feed.Feed

interface FeedRemoteDataSource {
    fun getFeed(page: Int): Single<List<Feed>>
}