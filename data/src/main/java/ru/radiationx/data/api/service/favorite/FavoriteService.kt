package ru.radiationx.data.api.service.favorite

import io.reactivex.Single
import ru.radiationx.data.api.remote.ReleaseResponse
import ru.radiationx.data.api.remote.common.ApiBaseResponse
import ru.radiationx.data.api.remote.common.handleApiResponse
import ru.radiationx.data.api.remote.pagination.PaginatedResponse

class FavoriteService(
    private val favoriteApi: FavoriteApi
) {

    fun getList(page: Int): Single<PaginatedResponse<ReleaseResponse>> = favoriteApi
        .getList(
            mapOf(
                "query" to "favorites",
                "page" to page.toString(),
                "filter" to "id,torrents,playlist,favorite,moon,blockedInfo",
                "rm" to "true"
            )
        )
        .handleApiResponse()

    fun add(releaseId: Int): Single<ReleaseResponse> = favoriteApi
        .add(
            mapOf(
                "query" to "favorites",
                "action" to "add",
                "id" to releaseId.toString()
            )
        )
        .handleApiResponse()

    fun delete(releaseId: Int): Single<ReleaseResponse> = favoriteApi
        .delete(
            mapOf(
                "query" to "favorites",
                "action" to "delete",
                "id" to releaseId.toString()
            )
        )
        .handleApiResponse()
}