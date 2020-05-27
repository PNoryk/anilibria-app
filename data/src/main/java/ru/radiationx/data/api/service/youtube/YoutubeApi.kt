package ru.radiationx.data.api.service.youtube

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.entity.youtube.YouTubeResponse
import ru.radiationx.data.api.entity.common.ApiBaseResponse
import ru.radiationx.data.api.entity.pagination.PaginatedResponse

interface YoutubeApi {

    fun getList(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<PaginatedResponse<YouTubeResponse>>>
}