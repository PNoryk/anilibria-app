package ru.radiationx.data.api.service.youtube

import io.reactivex.Single
import ru.radiationx.data.adomain.YouTube
import ru.radiationx.data.adomain.pagination.Paginated
import ru.radiationx.data.api.remote.YouTubeResponse
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.common.pagination.PaginatedResponse
import ru.radiationx.data.api.converter.PaginationConverter
import ru.radiationx.data.api.converter.YoutubeConverter

class YouTubeService(
    private val youtubeApi: YoutubeApi,
    private val youtubeConverter: YoutubeConverter,
    private val paginationConverter: PaginationConverter
) {

    fun getList(page: Int): Single<Paginated<YouTube>> = youtubeApi
        .getList(
            mapOf(
                "query" to "youtube",
                "page" to page.toString()
            )
        )
        .handleApiResponse()
        .map {
            paginationConverter.toDomain(it) { youTubeResponse ->
                youtubeConverter.toDomain(youTubeResponse)
            }
        }
}