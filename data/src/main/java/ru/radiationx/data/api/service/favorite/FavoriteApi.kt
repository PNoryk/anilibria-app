package ru.radiationx.data.api.service.favorite

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.remote.release.ReleaseResponse
import ru.radiationx.data.api.remote.common.ApiBaseResponse
import ru.radiationx.data.api.remote.pagination.PaginatedResponse

interface FavoriteApi {

    fun getList(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<PaginatedResponse<ReleaseResponse>>>
    fun add(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<ReleaseResponse>>
    fun delete(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<ReleaseResponse>>
}