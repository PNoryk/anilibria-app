package tv.anilibria.feature.content.data.remote.datasource.remote.api

import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.remote.datasource.remote.retrofit.FavoriteApiWrapper
import tv.anilibria.feature.content.data.remote.entity.mapper.toDomain
import tv.anilibria.feature.content.types.Page
import tv.anilibria.feature.content.types.release.Release
import tv.anilibria.feature.content.types.release.ReleaseId
import tv.anilibria.plugin.data.network.BaseUrlsProvider
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse

@InjectConstructor
class FavoriteRemoteDataSource(
    private val favoriteApi: FavoriteApiWrapper,
    private val urlsProvider: BaseUrlsProvider
) {

    suspend fun getFavorites(page: Int): Page<Release> {
        val args = formBodyOf(
            "query" to "favorites",
            "page" to page.toString(),
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return favoriteApi.proxy()
            .getFavorites(urlsProvider.api.value, args)
            .handleApiResponse()
            .toDomain {
                it.toDomain()
            }
    }

    suspend fun addFavorite(releaseId: ReleaseId): Release {
        val args = formBodyOf(
            "query" to "favorites",
            "action" to "add",
            "id" to releaseId.id.toString()
        )
        return favoriteApi.proxy()
            .addFavorite(urlsProvider.api.value, args)
            .handleApiResponse()
            .toDomain()
    }

    suspend fun deleteFavorite(releaseId: ReleaseId): Release {
        val args = formBodyOf(
            "query" to "favorites",
            "action" to "delete",
            "id" to releaseId.id.toString()
        )
        return favoriteApi.proxy()
            .deleteFavorite(urlsProvider.api.value, args)
            .handleApiResponse()
            .toDomain()
    }

}