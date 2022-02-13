package tv.anilibria.module.data.repos

import tv.anilibria.module.data.restapi.datasource.remote.api.FavoriteRemoteDataSource
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.release.Release
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val favoriteApi: FavoriteRemoteDataSource
) {

    suspend fun getFavorites(page: Int): Page<Release> {
        return favoriteApi.getFavorites(page)
    }

    suspend fun deleteFavorite(releaseId: Int): Release {
        return favoriteApi.deleteFavorite(releaseId)
    }

    suspend fun addFavorite(releaseId: Int): Release {
        return favoriteApi.addFavorite(releaseId)
    }
}