package ru.radiationx.data.api.datasource.impl

import io.reactivex.Single
import anilibria.tv.domain.entity.pagination.Paginated
import anilibria.tv.domain.entity.youtube.Youtube
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.PaginationConverter
import ru.radiationx.data.api.converter.YoutubeConverter
import ru.radiationx.data.api.datasource.YouTubeApiDataSource
import ru.radiationx.data.api.service.YoutubeService
import toothpick.InjectConstructor

@InjectConstructor
class YouTubeApiDataSourceImpl(
    private val youtubeService: YoutubeService,
    private val youtubeConverter: YoutubeConverter,
    private val paginationConverter: PaginationConverter
) : YouTubeApiDataSource {

    override fun getList(page: Int): Single<Paginated<Youtube>> = youtubeService
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