package ru.radiationx.data.api.service.feed

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.remote.FeedItemResponse
import ru.radiationx.data.api.common.ApiBaseResponse
import ru.radiationx.data.api.common.pagination.PaginatedResponse

interface FeedApi {

    fun getList(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<PaginatedResponse<FeedItemResponse>>>
}