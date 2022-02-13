package tv.anilibria.module.data.restapi.datasource.remote.api

import org.json.JSONObject
import tv.anilibria.module.data.restapi.datasource.remote.retrofit.SearchApi
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.network.NetworkWrapper
import tv.anilibria.plugin.data.restapi.handleApiResponse
import javax.inject.Inject

class SearchRemoteDataSource @Inject constructor(
    private val searchApi: NetworkWrapper<SearchApi>
) {

    suspend fun getGenres(): List<String> {
        val args = formBodyOf(
            "query" to "genres"
        )
        return searchApi.proxy()
            .getGenres(args)
            .handleApiResponse()
    }

    suspend fun getYears(): List<String> {
        val args = formBodyOf(
            "query" to "years"
        )
        return searchApi.proxy()
            .getYears(args)
            .handleApiResponse()
    }

    suspend fun fastSearch(name: String): List<Release> {
        val args = formBodyOf(
            "query" to "search",
            "search" to name,
            "filter" to "id,code,names,poster"
        )
        return searchApi.proxy()
            .fastSearch(args)
            .handleApiResponse()
            .map { it.toDomain() }
    }

    suspend fun searchReleases(
        genre: String,
        year: String,
        season: String,
        sort: String,
        complete: String,
        page: Int
    ): Page<Release> {
        val args = formBodyOf(
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
        return searchApi.proxy()
            .search(args)
            .handleApiResponse()
            .toDomain { it.toDomain() }
    }

}
