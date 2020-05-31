package anilibria.tv.api.impl.service

import io.reactivex.Single
import retrofit2.http.FieldMap
import anilibria.tv.api.impl.entity.common.ApiBaseResponse
import anilibria.tv.api.impl.entity.pagination.PaginatedResponse
import anilibria.tv.api.impl.entity.release.ReleaseResponse

interface SearchService {

    fun getYears(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<List<String>>>
    fun getGenres(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<List<String>>>
    fun getSuggestions(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<List<ReleaseResponse>>>
    fun getMatches(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<PaginatedResponse<ReleaseResponse>>>
}