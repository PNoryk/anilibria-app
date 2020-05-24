package ru.radiationx.data.api.service.search

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.remote.ReleaseResponse
import ru.radiationx.data.api.common.ApiBaseResponse
import ru.radiationx.data.api.common.pagination.PaginatedResponse

interface SearchApi {

    fun getYears(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<List<String>>>
    fun getGenres(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<List<String>>>
    fun getSuggestions(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<List<ReleaseResponse>>>
    fun getMatches(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<PaginatedResponse<ReleaseResponse>>>
}