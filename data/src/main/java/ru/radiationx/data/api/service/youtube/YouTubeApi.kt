package ru.radiationx.data.api.service.youtube

import io.reactivex.Single
import ru.radiationx.data.api.remote.YouTubeResponse
import ru.radiationx.data.api.remote.common.ApiBaseResponse
import ru.radiationx.data.api.remote.pagination.PaginatedResponse

interface YouTubeApi {
    fun getList(): Single<ApiBaseResponse<PaginatedResponse<YouTubeResponse>>>
}