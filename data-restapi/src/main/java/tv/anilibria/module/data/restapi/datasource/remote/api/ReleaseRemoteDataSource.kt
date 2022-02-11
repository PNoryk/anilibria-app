package tv.anilibria.module.data.restapi.datasource.remote.api

import io.reactivex.Single
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

    fun getRandomRelease(): Single<RandomRelease> {
        val args = formBodyOf(
            "query" to "random_release"
        )
        return releaseApi.proxy()
            .getRandom(args)
            .handleApiResponse()
            .map { it.toDomain() }
    }

    fun getRelease(releaseId: Int): Single<Release> {
        val args = formBodyOf(
            "query" to "release",
            "id" to releaseId.toString()
        )
        return releaseApi.proxy()
            .getRelease(args)
            .handleApiResponse()
            .map { it.toDomain() }
    }

    fun getRelease(releaseCode: String): Single<Release> {
        val args = formBodyOf(
            "query" to "release",
            "code" to releaseCode
        )
        return releaseApi.proxy()
            .getRelease(args)
            .handleApiResponse()
            .map { it.toDomain() }
    }

    fun getReleasesByIds(ids: List<Int>): Single<List<Release>> {
        val args = formBodyOf(
            "query" to "info",
            "id" to ids.joinToString(","),
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return releaseApi.proxy()
            .getReleases(args)
            .handleApiResponse()
            .map { items -> items.map { it.toDomain() } }
    }

    fun getReleases(page: Int): Single<Page<Release>> {
        val args = formBodyOf(
            "query" to "list",
            "page" to page.toString(),
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return releaseApi.proxy()
            .getPagesReleases(args)
            .handleApiResponse()
            .map { pageResponse -> pageResponse.toDomain { it.toDomain() } }
    }
}

        