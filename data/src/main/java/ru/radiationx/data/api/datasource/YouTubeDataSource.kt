package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.entity.youtube.Youtube
import ru.radiationx.data.adomain.entity.pagination.Paginated
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.PaginationConverter
import ru.radiationx.data.api.converter.YoutubeConverter
import ru.radiationx.data.api.service.YoutubeService
import toothpick.InjectConstructor

@InjectConstructor
class YouTubeDataSource(
    private val youtubeService: YoutubeService,
    private val youtubeConverter: YoutubeConverter,
    private val paginationConverter: PaginationConverter
) {

    fun getList(page: Int): Single<Paginated<Youtube>> = youtubeService
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