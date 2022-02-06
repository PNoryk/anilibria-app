package tv.anilibria.module.data.network.datasource.remote.api

import com.squareup.moshi.Moshi
import io.reactivex.Single
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfigProvider
import tv.anilibria.module.data.network.datasource.remote.mapApiResponse
import tv.anilibria.module.data.network.entity.app.PageResponse
import tv.anilibria.module.data.network.entity.app.release.RandomReleaseResponse
import tv.anilibria.module.data.network.entity.app.release.ReleaseResponse
import tv.anilibria.module.data.network.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.Page
import tv.anilibria.module.domain.entity.release.RandomRelease
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.module.domain.remote.ReleaseRemoteDataSource
import javax.inject.Inject

/* Created by radiationx on 31.10.17. */

class ReleaseRemoteDataSourceImpl @Inject constructor(
    @ApiClient private val client: IClient,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) : ReleaseRemoteDataSource {

    override fun getRandomRelease(): Single<RandomRelease> {
        val args = mapOf(
            "query" to "random_release"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<RandomReleaseResponse>(moshi)
            .map { it.toDomain() }
    }

    override fun getRelease(releaseId: Int): Single<Release> {
        val args = mapOf(
            "query" to "release",
            "id" to releaseId.toString()
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<ReleaseResponse>(moshi)
            .map { it.toDomain() }
    }

    override fun getRelease(releaseCode: String): Single<Release> {
        val args = mapOf(
            "query" to "release",
            "code" to releaseCode
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<ReleaseResponse>(moshi)
            .map { it.toDomain() }
    }

    override fun getReleasesByIds(ids: List<Int>): Single<List<Release>> {
        val args = mapOf(
            "query" to "info",
            "id" to ids.joinToString(","),
            "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
            "rm" to "true"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<List<ReleaseResponse>>(moshi)
            .map { items -> items.map { it.toDomain() } }
    }

    override fun getReleases(page: Int): Single<Page<Release>> {
        val args = mapOf(
            "query" to "list",
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

        