package ru.radiationx.data.api.service.youtube

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.remote.YouTubeResponse
import ru.radiationx.data.api.remote.common.ApiBaseResponse
import ru.radiationx.data.api.remote.common.handleApiResponse
import ru.radiationx.data.api.remote.pagination.PaginatedResponse

class YouTubeService(
    private val youtubeApi: YoutubeApi
) {

    fun getList(page: Int): Single<PaginatedResponse<YouTubeResponse>> = youtubeApi
        .getList(
            mapOf(
                "query" to "youtube",
                "page" to page.toString()
            )
        )
        .handleApiResponse()
}