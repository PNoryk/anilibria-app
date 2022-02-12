package tv.anilibria.module.data.restapi.datasource.remote.api

import tv.anilibria.module.data.restapi.datasource.remote.retrofit.FavoriteApi
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.ApiWrapper
import tv.anilibria.plugin.data.restapi.handleApiResponse
import javax.inject.Inject

class FavoriteRemoteDataSource @Inject constructor(
    private val favoriteApi: ApiWrapper<FavoriteApi>,
) {

    suspend fun getFavorites(page: Int): Page<Release> {
        val args = formBodyOf(
            "query" to "favorites",
            "page" to page.toString(),
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return favoriteApi.proxy()
            .getFavorites(args)
            .handleApiResponse()
            .toDomain {
                it.toDomain()
            }
    }

    suspend fun addFavorite(releaseId: Int): Release {
        val args = formBodyOf(
            "query" to "favorites",
            "action" to "add",
            "id" to releaseId.toString()
        )
        return favoriteApi.proxy()
            .addFavorite(args)
            .handleApiResponse()
            .toDomain()
    }

    suspend fun deleteFavorite(releaseId: Int): Release {
        val args = formBodyOf(
            "query" to "favorites",
            "action" to "delete",
            "id" to releaseId.toString()
        )
        return favoriteApi.proxy()
            .deleteFavorite(args)
            .handleApiResponse()
            .toDomain()
    }

}