package tv.anilibria.feature.content.data.repos

import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.local.CacheUpdateStrategy
import tv.anilibria.feature.content.data.local.ReleaseCacheHelper
import tv.anilibria.feature.content.data.local.ReleaseUpdateHelper
import tv.anilibria.feature.content.data.local.holders.ReleaseCacheLocalDataSource
import tv.anilibria.feature.content.data.remote.datasource.remote.api.FeedRemoteDataSource
import tv.anilibria.feature.content.types.feed.Feed

@InjectConstructor
class FeedRepository(
    private val feedApi: FeedRemoteDataSource,
    private val releaseUpdateHelper: ReleaseUpdateHelper,
    private val releaseCacheHelper: ReleaseCacheHelper,
    private val releaseCache: ReleaseCacheLocalDataSource
) {

    suspend fun getFeed(page: Int): List<Feed> {
        return feedApi.getFeed(page).also {
            val releases = it.mapNotNull { it.release }
            releaseCacheHelper.update(releases, CacheUpdateStrategy.MERGE)
            releaseUpdateHelper.update(releases)
        }.let { feedData ->
            val releases = releaseCache.get()
            feedData.map { feed ->
                feed.copy(
                    release = feed.release?.let { releases[it.id] } ?: feed.release
                )
            }
        }
    }

}