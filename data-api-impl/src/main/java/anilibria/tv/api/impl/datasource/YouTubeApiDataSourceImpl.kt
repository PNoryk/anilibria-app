package anilibria.tv.api.impl.datasource

import io.reactivex.Single
import anilibria.tv.domain.entity.pagination.Paginated
import anilibria.tv.domain.entity.youtube.Youtube
import anilibria.tv.api.impl.common.handleApiResponse
import anilibria.tv.api.impl.converter.PaginationConverter
import anilibria.tv.api.impl.converter.YoutubeConverter
import anilibria.tv.api.YouTubeApiDataSource
import anilibria.tv.api.impl.service.YoutubeService
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