package ru.radiationx.data.api.service

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.entity.common.ApiBaseResponse
import ru.radiationx.data.api.entity.pagination.PaginatedResponse
import ru.radiationx.data.api.entity.release.RandomReleaseResponse
import ru.radiationx.data.api.entity.release.ReleaseResponse

interface ReleaseService {
    fun getOne(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<ReleaseResponse>>
    fun getSome(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<List<ReleaseResponse>>>
    fun getList(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<PaginatedResponse<ReleaseResponse>>>
    fun getRandom(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<RandomReleaseResponse>>
}