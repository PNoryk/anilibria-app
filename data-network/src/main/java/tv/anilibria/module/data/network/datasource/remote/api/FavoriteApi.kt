package tv.anilibria.module.data.network.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfigProvider
import tv.anilibria.module.data.network.datasource.remote.mapApiResponse
import tv.anilibria.module.data.network.entity.app.PageResponse
import tv.anilibria.module.data.network.entity.app.release.ReleaseResponse
import javax.inject.Inject

class FavoriteApi @Inject constructor(
    @ApiClient private val client: IClient,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) {

    fun getFavorites(page: Int): Single<PageResponse<ReleaseResponse>> {
        val args: MutableMap<String, String> = mutableMapOf(
            "query" to "favorites",
            "page" to page.toString(),
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse(moshi)
    }

    fun addFavorite(releaseId: Int): Single<ReleaseResponse> {
        val args: MutableMap<String, String> = mutableMapOf(
            "query" to "favorites",
            "action" to "add",
            "id" to releaseId.toString()
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse(moshi)
    }

    fun deleteFavorite(releaseId: Int): Single<ReleaseResponse> {
        val args: MutableMap<String, String> = mutableMapOf(
            "query" to "favorites",
            "action" to "delete",
            "id" to releaseId.toString()
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse(moshi)
    }

}