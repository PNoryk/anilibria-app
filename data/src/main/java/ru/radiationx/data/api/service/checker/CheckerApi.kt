package ru.radiationx.data.api.service.checker

import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FieldMap
import ru.radiationx.data.api.remote.checker.CheckerResponse
import ru.radiationx.data.api.remote.common.ApiBaseResponse

interface CheckerApi {

    fun get(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<CheckerResponse>>
}