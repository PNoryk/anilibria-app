package ru.radiationx.data.api.service.feed

import io.reactivex.Single
import ru.radiationx.data.api.remote.FeedItemResponse
import ru.radiationx.data.api.remote.common.ApiBaseResponse
import ru.radiationx.data.api.remote.pagination.PaginatedResponse

interface FeedApi {

    fun getList(): Single<ApiBaseResponse<PaginatedResponse<FeedItemResponse>>>
}