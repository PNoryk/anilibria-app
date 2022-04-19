package tv.anilibria.feature.content.data.remote.datasource.remote.api

import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.remote.datasource.remote.retrofit.ReleaseApiWrapper
import tv.anilibria.feature.content.data.remote.entity.mapper.toDomain
import tv.anilibria.feature.content.types.Page
import tv.anilibria.feature.content.types.release.RandomRelease
import tv.anilibria.feature.content.types.release.Release
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.handleApiResponse

@InjectConstructor
class ReleaseRemoteDataSource(
    private val releaseApi: ReleaseApiWrapper
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

    suspend fun getRelease(releaseId: Long): Release {
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

    suspend fun getReleasesByIds(ids: List<Long>): List<Release> {
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

        