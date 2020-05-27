package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.entity.release.RandomRelease
import ru.radiationx.data.adomain.entity.release.Release
import ru.radiationx.data.adomain.entity.pagination.Paginated
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.PaginationConverter
import ru.radiationx.data.api.converter.ReleaseConverter
import ru.radiationx.data.api.service.ReleaseService
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseDataSource(
    private val releaseService: ReleaseService,
    private val releaseConverter: ReleaseConverter,
    private val paginationConverter: PaginationConverter
) {

    fun getOne(releaseId: Int? = null, releaseCode: String? = null): Single<Release> {
        if (releaseId == null && releaseCode == null) {
            throw IllegalArgumentException("Release id and code is null")
        }

        val params = mutableMapOf("query" to "release")
        releaseId?.also { params["id"] = it.toString() }
        releaseCode?.also { params["code"] = it }
        return releaseService
            .getOne(params)
            .handleApiResponse()
            .map { releaseConverter.toDomain(it) }
    }

    fun getSome(ids: List<Int>? = null, codes: List<String>? = null): Single<List<Release>> {
        if (ids == null && codes == null) {
            throw IllegalArgumentException("Release ids and codes is null")
        }

        val params = mutableMapOf("query" to "release")
        ids?.also { params["id"] = it.joinToString(",") }
        codes?.also { params["code"] = it.joinToString(",") }
        return releaseService
            .getSome(params)
            .handleApiResponse()
            .map {
                it.map { releaseResponse ->
                    releaseConverter.toDomain(releaseResponse)
                }
            }
    }

    fun getList(page: Int): Single<Paginated<Release>> = releaseService
        .getList(
            mapOf(
                "query" to "list",
                "page" to page.toString(),
                "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
                "rm" to "true"
            )
        )
        .handleApiResponse()
        .map {
            paginationConverter.toDomain(it) { releaseResponse ->
                releaseConverter.toDomain(releaseResponse)
            }
        }

    fun getRandom(): Single<RandomRelease> = releaseService
        .getRandom(mapOf("query" to "random_release"))
        .handleApiResponse()
        .map { releaseConverter.toDomain(it) }
}