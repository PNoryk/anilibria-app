package ru.radiationx.data.api.service.youtube

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.remote.youtube.YouTubeResponse
import ru.radiationx.data.api.remote.common.ApiBaseResponse
import ru.radiationx.data.api.remote.pagination.PaginatedResponse

interface YoutubeApi {

    fun getList(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<PaginatedResponse<YouTubeResponse>>>
}