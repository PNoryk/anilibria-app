package ru.radiationx.data.api.service.release

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.remote.RandomReleaseResponse
import ru.radiationx.data.api.remote.ReleaseResponse
import ru.radiationx.data.api.common.ApiBaseResponse
import ru.radiationx.data.api.common.pagination.PaginatedResponse

interface ReleaseApi {
    fun getOne(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<ReleaseResponse>>
    fun getSome(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<List<ReleaseResponse>>>
    fun getList(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<PaginatedResponse<ReleaseResponse>>>
    fun getRandom(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<RandomReleaseResponse>>
}