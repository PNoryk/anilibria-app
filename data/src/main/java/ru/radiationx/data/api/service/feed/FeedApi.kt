package ru.radiationx.data.api.service.feed

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.entity.feed.FeedItemResponse
import ru.radiationx.data.api.entity.common.ApiBaseResponse
import ru.radiationx.data.api.entity.pagination.PaginatedResponse

interface FeedApi {

    fun getList(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<PaginatedResponse<FeedItemResponse>>>
}