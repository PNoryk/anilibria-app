package tv.anilibria.module.data.restapi.datasource.remote.api

import tv.anilibria.module.data.restapi.datasource.remote.retrofit.ReleaseApi
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.release.RandomRelease
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.ApiWrapper
import tv.anilibria.plugin.data.restapi.handleApiResponse
import javax.inject.Inject

/* Created by radiationx on 31.10.17. */

class ReleaseRemoteDataSource @Inject constructor(
    private val releaseApi: ApiWrapper<ReleaseApi>
) {

    suspend fun getRandomRelease(): RandomRelease {
        val args = formBodyOf(
            "query" to "random_release"
        )
        return releaseApi.proxy()
            .getRandom(args)
            .handleApiResponse()
            .toDomain()
    }

    suspend fun getRelease(releaseId: Int): Release {
        val args = formBodyOf(
            "query" to "release",
            "id" to releaseId.toString()
        )
        return releaseApi.proxy()
            .getRelease(args)
            .handleApiResponse()
            .toDomain()
    }

    suspend fun getRelease(releaseCode: String): Release {
        val args = formBodyOf(
            "query" to "release",
            "code" to releaseCode
        )
        return releaseApi.proxy()
            .getRelease(args)
            .handleApiResponse()
            .toDomain()
    }

    suspend fun getReleasesByIds(ids: List<Int>): List<Release> {
        val args = formBodyOf(
            "query" to "info",
            "id" to ids.joinToString(","),
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return releaseApi.proxy()
            .getReleases(args)
            .handleApiResponse()
            .map { it.toDomain() }
    }

    suspend fun getReleases(page: Int): Page<Release> {
        val args = formBodyOf(
            "query" to "list",
            "page" to page.toString(),
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return releaseApi.proxy()
            .getPagesReleases(args)
            .handleApiResponse()
            .toDomain { it.toDomain() }
    }
}

        