package tv.anilibria.feature.content.data.repos

import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.local.ReleaseUpdateHelper
import tv.anilibria.feature.content.data.remote.datasource.remote.api.FeedRemoteDataSource
import tv.anilibria.feature.content.types.feed.Feed

@InjectConstructor
class FeedRepository(
    private val feedApi: FeedRemoteDataSource,
    private val releaseUpdateHolder: ReleaseUpdateHelper
) {

    suspend fun getFeed(page: Int): List<Feed> {
        return feedApi.getFeed(page).also {
            releaseUpdateHolder.update(it.mapNotNull { it.release })
        }
    }

}