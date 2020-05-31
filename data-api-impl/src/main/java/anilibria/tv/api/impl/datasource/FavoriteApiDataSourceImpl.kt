package anilibria.tv.api.impl.datasource

import io.reactivex.Single
import anilibria.tv.domain.entity.pagination.Paginated
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.api.impl.common.handleApiResponse
import anilibria.tv.api.impl.converter.PaginationConverter
import anilibria.tv.api.impl.converter.ReleaseConverter
import anilibria.tv.api.FavoriteApiDataSource
import anilibria.tv.api.impl.service.FavoriteService
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteApiDataSourceImpl(
    private val favoriteService: FavoriteService,
    private val releaseConverter: ReleaseConverter,
    private val paginationConverter: PaginationConverter
) : FavoriteApiDataSource {

    override fun getList(page: Int): Single<Paginated<Release>> = favoriteService
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

    override fun add(releaseId: Int): Single<Release> = favoriteService
        .add(
            mapOf(
                "query" to "favorites",
                "action" to "add",
                "id" to releaseId.toString()
            )
        )
        .handleApiResponse()
        .map { releaseConverter.toDomain(it) }

    override fun delete(releaseId: Int): Single<Release> = favoriteService
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