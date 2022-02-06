package tv.anilibria.module.data.network.datasource.remote.api

import io.reactivex.Single
import org.json.JSONArray
import org.json.JSONObject
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.ApiResponse
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfig
import tv.anilibria.module.data.network.datasource.remote.parsers.ReleaseParser
import tv.anilibria.module.data.network.entity.app.PageResponse
import tv.anilibria.module.data.network.entity.app.release.RandomReleaseResponse
import tv.anilibria.module.data.network.entity.app.release.ReleaseResponse
import javax.inject.Inject

/* Created by radiationx on 31.10.17. */

class ReleaseApi @Inject constructor(
    @ApiClient private val client: IClient,
    private val releaseParser: ReleaseParser,
    private val apiConfig: ApiConfig
) {

    fun getRandomRelease(): Single<RandomReleaseResponse> {
        val args: MutableMap<String, String> = mutableMapOf(
            "query" to "random_release"
        )
        return client.post(apiConfig.apiUrl, args)
            .compose(ApiResponse.fetchResult<JSONObject>())
            .map { releaseParser.parseRandomRelease(it) }
    }

    fun getRelease(releaseId: Int): Single<ReleaseResponse> {
        val args: MutableMap<String, String> = mutableMapOf(
            "query" to "release",
            "id" to releaseId.toString()
        )
        return client.post(apiConfig.apiUrl, args)
            .compose(ApiResponse.fetchResult<JSONObject>())
            .map { releaseParser.parseRelease(it) }
    }

    fun getRelease(releaseCode: String): Single<ReleaseResponse> {
        val args: MutableMap<String, String> = mutableMapOf(
            "query" to "release",
            "code" to releaseCode
        )
        return client.post(apiConfig.apiUrl, args)
            .compose(ApiResponse.fetchResult<JSONObject>())
            .map { releaseParser.parseRelease(it) }
    }

    fun getReleasesByIds(ids: List<Int>): Single<List<ReleaseResponse>> {
        val args: MutableMap<String, String> = mutableMapOf(
            "query" to "info",
            "id" to ids.joinToString(","),
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return client.post(apiConfig.apiUrl, args)
            .compose(ApiResponse.fetchResult<JSONArray>())
            .map { releaseParser.releases(it) }
    }

    fun getReleases(page: Int): Single<PageResponse<ReleaseResponse>> {
        val args: MutableMap<String, String> = mutableMapOf(
            "query" to "list",
            "page" to page.toString(),
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return client.post(apiConfig.apiUrl, args)
            .compose(ApiResponse.fetchResult<JSONObject>())
            .map { releaseParser.releases(it) }
    }


}

        