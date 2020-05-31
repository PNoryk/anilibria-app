package anilibria.tv.api.impl.service

import io.reactivex.Single
import retrofit2.http.FieldMap
import anilibria.tv.api.impl.entity.common.ApiBaseResponse
import anilibria.tv.api.impl.entity.pagination.PaginatedResponse
import anilibria.tv.api.impl.entity.release.RandomReleaseResponse
import anilibria.tv.api.impl.entity.release.ReleaseResponse

interface ReleaseService {
    fun getOne(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<ReleaseResponse>>
    fun getSome(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<List<ReleaseResponse>>>
    fun getList(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<PaginatedResponse<ReleaseResponse>>>
    fun getRandom(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<RandomReleaseResponse>>
}