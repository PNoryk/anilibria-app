package ru.radiationx.data.api.service.release

import io.reactivex.Single
import ru.radiationx.data.api.remote.RandomReleaseResponse
import ru.radiationx.data.api.remote.ReleaseResponse
import ru.radiationx.data.api.remote.common.ApiBaseResponse
import ru.radiationx.data.api.remote.pagination.PaginatedResponse

interface ReleaseApi {
    fun getOne(): Single<ApiBaseResponse<ReleaseResponse>>
    fun getSome(): Single<ApiBaseResponse<List<ReleaseResponse>>>
    fun getList(): Single<ApiBaseResponse<PaginatedResponse<ReleaseResponse>>>
    fun getRandom(): Single<ApiBaseResponse<RandomReleaseResponse>>
}