package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.entity.pagination.Paginated
import ru.radiationx.data.adomain.entity.release.Release
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.PaginationConverter
import ru.radiationx.data.api.converter.ReleaseConverter
import ru.radiationx.data.api.service.FavoriteService
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteApiDataSource(
    private val favoriteService: FavoriteService,
    private val releaseConverter: ReleaseConverter,
    private val paginationConverter: PaginationConverter
) {

    fun getList(page: Int): Single<Paginated<Release>> = favoriteService
        .getList(
            mapOf(
                "query" to "favorites",
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

    fun add(releaseId: Int): Single<Release> = favoriteService
        .add(
            mapOf(
                "query" to "favorites",
                "action" to "add",
                "id" to releaseId.toString()
            )
        )
        .handleApiResponse()
        .map { releaseConverter.toDomain(it) }

    fun delete(releaseId: Int): Single<Release> = favoriteService
        .delete(
            mapOf(
                "query" to "favorites",
                "action" to "delete",
                "id" to releaseId.toString()
            )
        )
        .handleApiResponse()
        .map { releaseConverter.toDomain(it) }
}