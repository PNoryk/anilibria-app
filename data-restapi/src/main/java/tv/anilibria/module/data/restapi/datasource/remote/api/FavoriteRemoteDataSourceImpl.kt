package tv.anilibria.module.data.restapi.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.restapi.ApiClient
import tv.anilibria.module.data.network.NetworkClient
import tv.anilibria.module.data.restapi.datasource.remote.ApiConfigProvider
import tv.anilibria.module.data.restapi.datasource.remote.mapApiResponse
import tv.anilibria.module.data.restapi.entity.app.PageResponse
import tv.anilibria.module.data.restapi.entity.app.release.ReleaseResponse
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.module.domain.remote.FavoriteRemoteDataSource
import javax.inject.Inject

class FavoriteRemoteDataSourceImpl @Inject constructor(
    @ApiClient private val client: NetworkClient,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) : FavoriteRemoteDataSource {

    override fun getFavorites(page: Int): Single<Page<Release>> {
        val args = mapOf(
            "query" to "favorites",
            "page" to page.toString(),
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<PageResponse<ReleaseResponse>>(moshi)
            .map { pageResponse ->
                pageResponse.toDomain {
                    it.toDomain()
                }
            }
    }

    override fun addFavorite(releaseId: Int): Single<Release> {
        val args = mapOf(
            "query" to "favorites",
            "action" to "add",
            "id" to releaseId.toString()
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<ReleaseResponse>(moshi)
            .map { it.toDomain() }
    }

    override fun deleteFavorite(releaseId: Int): Single<Release> {
        val args = mapOf(
            "query" to "favorites",
            "action" to "delete",
            "id" to releaseId.toString()
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<ReleaseResponse>(moshi)
            .map { it.toDomain() }
    }

}