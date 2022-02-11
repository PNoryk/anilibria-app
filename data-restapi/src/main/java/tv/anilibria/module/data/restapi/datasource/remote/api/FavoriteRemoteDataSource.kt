package tv.anilibria.module.data.restapi.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.restapi.datasource.remote.retrofit.FavoriteApi
import tv.anilibria.module.data.restapi.entity.app.PageResponse
import tv.anilibria.module.data.restapi.entity.app.release.ReleaseResponse
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.*
import javax.inject.Inject

class FavoriteRemoteDataSource @Inject constructor(
    private val apiClient: ApiNetworkClient,
    private val apiConfig: ApiConfigProvider,
    private val favoriteApi: ApiWrapper<FavoriteApi>,
    private val moshi: Moshi
) {

    fun getFavorites(page: Int): Single<Page<Release>> {
        val args = formBodyOf(
            "query" to "favorites",
            "page" to page.toString(),
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return favoriteApi.proxy()
            .getFavorites(args)
            .handleApiResponse()
            .map { pageResponse ->
                pageResponse.toDomain {
                    it.toDomain()
                }
            }
    }

    fun addFavorite(releaseId: Int): Single<Release> {
        val args = formBodyOf(
            "query" to "favorites",
            "action" to "add",
            "id" to releaseId.toString()
        )
        return favoriteApi.proxy()
            .addFavorite(args)
            .handleApiResponse()
            .map { it.toDomain() }
    }

    fun deleteFavorite(releaseId: Int): Single<Release> {
        val args = formBodyOf(
            "query" to "favorites",
            "action" to "delete",
            "id" to releaseId.toString()
        )
        return favoriteApi.proxy()
            .deleteFavorite(args)
            .handleApiResponse()
            .map { it.toDomain() }
    }

}