package ru.radiationx.data.api.service.youtube

import io.reactivex.Single
import ru.radiationx.data.adomain.youtube.Youtube
import ru.radiationx.data.adomain.pagination.Paginated
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.PaginationConverter
import ru.radiationx.data.api.converter.YoutubeConverter
import toothpick.InjectConstructor

@InjectConstructor
class YouTubeService(
    private val youtubeApi: YoutubeApi,
    private val youtubeConverter: YoutubeConverter,
    private val paginationConverter: PaginationConverter
) {

    fun getList(page: Int): Single<Paginated<Youtube>> = youtubeApi
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