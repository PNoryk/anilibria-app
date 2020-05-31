package anilibria.tv.api.impl.service

import io.reactivex.Single
import retrofit2.http.FieldMap
import anilibria.tv.api.impl.entity.common.ApiBaseResponse
import anilibria.tv.api.impl.entity.pagination.PaginatedResponse
import anilibria.tv.api.impl.entity.release.ReleaseResponse

interface FavoriteService {

    fun getList(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<PaginatedResponse<ReleaseResponse>>>
    fun add(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<ReleaseResponse>>
    fun delete(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<ReleaseResponse>>
}