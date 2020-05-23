package ru.radiationx.data.api.service.search

import io.reactivex.Single
import ru.radiationx.data.api.remote.ReleaseResponse
import ru.radiationx.data.api.remote.common.ApiBaseResponse
import ru.radiationx.data.api.remote.pagination.PaginatedResponse

interface SearchApi {

    fun getYears(): Single<ApiBaseResponse<List<String>>>
    fun getGenres(): Single<ApiBaseResponse<List<String>>>
    fun getSuggestions(): Single<ApiBaseResponse<List<ReleaseResponse>>>
    fun getMatches(): Single<ApiBaseResponse<PaginatedResponse<ReleaseResponse>>>
}