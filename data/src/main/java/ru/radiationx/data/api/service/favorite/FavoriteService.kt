package ru.radiationx.data.api.service.favorite

import io.reactivex.Single
import ru.radiationx.data.adomain.Release
import ru.radiationx.data.adomain.pagination.Paginated
import ru.radiationx.data.api.remote.ReleaseResponse
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.common.pagination.PaginatedResponse
import ru.radiationx.data.api.converter.PaginationConverter
import ru.radiationx.data.api.converter.ReleaseConverter
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteService(
    private val favoriteApi: FavoriteApi,
    private val releaseConverter: ReleaseConverter,
    private val paginationConverter: PaginationConverter
) {

    fun getList(page: Int): Single<Paginated<Release>> = favoriteApi
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

    fun add(releaseId: Int): Single<Release> = favoriteApi
        .add(
            mapOf(
                "query" to "favorites",
                "action" to "add",
                "id" to releaseId.toString()
            )
        )
        .handleApiResponse()
        .map { releaseConverter.toDomain(it) }

    fun delete(releaseId: Int): Single<Release> = favoriteApi
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