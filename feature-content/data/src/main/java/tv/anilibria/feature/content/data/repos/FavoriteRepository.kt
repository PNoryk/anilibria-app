package tv.anilibria.feature.content.data.repos

import tv.anilibria.feature.content.data.local.ReleaseUpdateHelper
import tv.anilibria.feature.content.data.remote.datasource.remote.api.FavoriteRemoteDataSource
import tv.anilibria.feature.domain.entity.Page
import tv.anilibria.feature.domain.entity.release.Release
import tv.anilibria.feature.domain.entity.release.ReleaseId
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
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