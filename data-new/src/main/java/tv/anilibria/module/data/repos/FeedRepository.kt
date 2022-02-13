package tv.anilibria.module.data.repos

import tv.anilibria.module.data.ReleaseUpdateHolder
import tv.anilibria.module.data.restapi.datasource.remote.api.FeedRemoteDataSource
import tv.anilibria.module.domain.entity.feed.Feed
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val feedApi: FeedRemoteDataSource,
    private val releaseUpdateHolder: ReleaseUpdateHolder
) {

    suspend fun getFeed(page: Int): List<Feed> {
        return feedApi.getFeed(page).also {
            releaseUpdateHolder.update(it.mapNotNull { it.release })
        }
    }

}