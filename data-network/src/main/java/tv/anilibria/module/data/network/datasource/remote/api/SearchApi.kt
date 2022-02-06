package tv.anilibria.module.data.network.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import org.json.JSONObject
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfigProvider
import tv.anilibria.module.data.network.datasource.remote.mapApiResponse
import tv.anilibria.module.data.network.entity.app.PageResponse
import tv.anilibria.module.data.network.entity.app.release.ReleaseResponse
import tv.anilibria.module.data.network.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.release.Release
import javax.inject.Inject

class SearchApi @Inject constructor(
    @ApiClient private val client: IClient,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) {

    fun getGenres(): Single<List<String>> {
        val args = mapOf(
            "query" to "genres"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse(moshi)
    }

    fun getYears(): Single<List<String>> {
        val args = mapOf(
            "query" to "years"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse(moshi)
    }

    fun fastSearch(name: String): Single<List<Release>> {
        val args = mapOf(
            "query" to "search",
            "search" to name,
            "filter" to "id,code,names,poster"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<List<ReleaseResponse>>(moshi)
            .map { items -> items.map { it.toDomain() } }
    }

    fun searchReleases(
        genre: String,
        year: String,
        season: String,
        sort: String,
        complete: String,
        page: Int
    ): Single<Page<Release>> {
        val args = mapOf(
            "query" to "catalog",
            "search" to JSONObject().apply {
                put("genre", genre)
                put("year", year)
                put("season", season)
            }.toString(),
            "finish" to complete,
            "xpage" to "catalog",
            "sort" to sort,
            "page" to page.toString(),
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<PageResponse<ReleaseResponse>>(moshi)
            .map { pageResponse -> pageResponse.toDomain { it.toDomain() } }
    }

}
