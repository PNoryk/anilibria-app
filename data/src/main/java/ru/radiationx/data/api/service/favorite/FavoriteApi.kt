package ru.radiationx.data.api.service.favorite

import io.reactivex.Single
import ru.radiationx.data.api.remote.ReleaseResponse
import ru.radiationx.data.api.remote.common.ApiBaseResponse
import ru.radiationx.data.api.remote.pagination.PaginatedResponse

interface FavoriteApi {

    fun getList(): Single<ApiBaseResponse<PaginatedResponse<ReleaseResponse>>>
    fun add(): Single<ApiBaseResponse<ReleaseResponse>>
    fun delete(): Single<ApiBaseResponse<ReleaseResponse>>
}