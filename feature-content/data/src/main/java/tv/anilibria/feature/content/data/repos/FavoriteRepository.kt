package tv.anilibria.feature.content.data.repos

import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.local.ReleaseUpdateHelper
import tv.anilibria.feature.content.data.remote.datasource.remote.api.FavoriteRemoteDataSource
import tv.anilibria.feature.content.types.Page
import tv.anilibria.feature.content.types.release.Release
import tv.anilibria.feature.content.types.release.ReleaseId

@InjectConstructor
class FavoriteRepository(
    private val favoriteApi: FavoriteRemoteDataSource,
    private val releaseUpdateHelper: ReleaseUpdateHelper
) {

    suspend fun getFavorites(page: Int): Page<Release> {
        return favoriteApi.getFavorites(page).also {
            releaseUpdateHelper.update(it.items)
        }
    }

    suspend fun deleteFavorite(releaseId: ReleaseId): Release {
        return favoriteApi.deleteFavorite(releaseId).also {
            releaseUpdateHelper.update(listOf(it))
        }
    }

    suspend fun addFavorite(releaseId: ReleaseId): Release {
        return favoriteApi.addFavorite(releaseId).also {
            releaseUpdateHelper.update(listOf(it))
        }
    }
}