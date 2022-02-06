package tv.anilibria.module.data.network.datasource.remote.api

import io.reactivex.Single
import org.json.JSONArray
import org.json.JSONObject
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.ApiResponse
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfigProvider
import tv.anilibria.module.data.network.datasource.remote.parsers.ReleaseParser
import tv.anilibria.module.data.network.datasource.remote.parsers.SearchParser
import tv.anilibria.module.data.network.entity.app.PageResponse
import tv.anilibria.module.data.network.entity.app.release.ReleaseResponse
import javax.inject.Inject

class SearchApi @Inject constructor(
    @ApiClient private val client: IClient,
    private val releaseParser: ReleaseParser,
    private val searchParser: SearchParser,
    private val apiConfig: ApiConfigProvider
) {

    fun getGenres(): Single<List<String>> {
        val args: MutableMap<String, String> = mutableMapOf(
            "query" to "genres"
        )
        return client.post(apiConfig.apiUrl, args)
            .compose(ApiResponse.fetchResult<JSONArray>())
            .map { searchParser.genres(it) }
    }

    fun getYears(): Single<List<String>> {
        val args: MutableMap<String, String> = mutableMapOf(
            "query" to "years"
        )
        return client.post(apiConfig.apiUrl, args)
            .compose(ApiResponse.fetchResult<JSONArray>())
            .map { searchParser.years(it) }
    }

    fun fastSearch(name: String): Single<List<ReleaseResponse>> {
        val args: MutableMap<String, String> = mutableMapOf(
            "query" to "search",
            "search" to name,
            "filter" to "id,code,names,poster"
        )
        return client.post(apiConfig.apiUrl, args)
            .compose(ApiResponse.fetchResult<JSONArray>())
            .map { releaseParser.releases(it) }
    }

    fun searchReleases(
        genre: String,
        year: String,
        season: String,
        sort: String,
        complete: String,
        page: Int
    ): Single<PageResponse<ReleaseResponse>> {
        val args: MutableMap<String, String> = mutableMapOf(
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
        return client.post(apiConfig.apiUrl, args)
            .compose(ApiResponse.fetchResult<JSONObject>())
            .map { releaseParser.releases(it) }
    }

}
