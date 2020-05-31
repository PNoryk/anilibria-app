package anilibria.tv.api.impl.service

import io.reactivex.Single
import retrofit2.http.FieldMap
import anilibria.tv.api.impl.entity.checker.CheckerResponse
import anilibria.tv.api.impl.entity.common.ApiBaseResponse

interface CheckerService {

    fun get(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<CheckerResponse>>
}