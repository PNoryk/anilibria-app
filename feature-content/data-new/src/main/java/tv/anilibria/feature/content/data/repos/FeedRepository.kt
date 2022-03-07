package tv.anilibria.feature.content.data.repos

import tv.anilibria.feature.content.data.local.ReleaseUpdateHelper
import tv.anilibria.feature.content.data.restapi.datasource.remote.api.FeedRemoteDataSource
import tv.anilibria.feature.domain.entity.feed.Feed
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val feedApi: FeedRemoteDataSource,
    private val releaseUpdateHolder: ReleaseUpdateHelper
) {

    suspend fun getFeed(page: Int): List<Feed> {
        return feedApi.getFeed(page).also {
            releaseUpdateHolder.update(it.mapNotNull { it.release })
        }
    }

}