package tv.anilibria.module.domain.remote

import io.reactivex.Single
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.release.Release

interface FavoriteRemoteDataSource {
    fun getFavorites(page: Int): Single<Page<Release>>
    fun addFavorite(releaseId: Int): Single<Release>
    fun deleteFavorite(releaseId: Int): Single<Release>
}