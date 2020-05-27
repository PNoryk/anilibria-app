package ru.radiationx.data.api.service

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.entity.checker.CheckerResponse
import ru.radiationx.data.api.entity.common.ApiBaseResponse

interface CheckerService {

    fun get(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<CheckerResponse>>
}