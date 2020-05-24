package ru.radiationx.data.api.service.release

import io.reactivex.Single
import ru.radiationx.data.api.remote.RandomReleaseResponse
import ru.radiationx.data.api.remote.ReleaseResponse
import ru.radiationx.data.api.remote.common.ApiBaseResponse
import ru.radiationx.data.api.remote.common.handleApiResponse
import ru.radiationx.data.api.remote.pagination.PaginatedResponse
import java.lang.RuntimeException

class ReleaseService(
    private val releaseApi: ReleaseApi
) {

    fun getOne(releaseId: Int? = null, releaseCode: String? = null): Single<ReleaseResponse> {
        if (releaseId == null && releaseCode == null) {
            throw IllegalArgumentException("Release id and code is null")
        }

        val params = mutableMapOf("query" to "release")
        releaseId?.also { params["id"] = it.toString() }
        releaseCode?.also { params["code"] = it }
        return releaseApi
            .getOne(params)
            .handleApiResponse()
    }

    fun getSome(ids: List<Int>? = null, codes: List<String>? = null): Single<List<ReleaseResponse>> {
        if (ids == null && codes == null) {
            throw IllegalArgumentException("Release ids and codes is null")
        }

        val params = mutableMapOf("query" to "release")
        ids?.also { params["id"] = it.joinToString(",") }
        codes?.also { params["code"] = it.joinToString(",") }
        return releaseApi
            .getSome(params)
            .handleApiResponse()
    }

    fun getList(page: Int): Single<PaginatedResponse<ReleaseResponse>> = releaseApi
        .getList(
            mapOf(
                "query" to "list",
                "page" to page.toString(),
                "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
                "rm" to "true"
            )
        )
        .handleApiResponse()

    fun getRandom(): Single<RandomReleaseResponse> = releaseApi
        .getRandom(mapOf("query" to "random_release"))
        .handleApiResponse()
}