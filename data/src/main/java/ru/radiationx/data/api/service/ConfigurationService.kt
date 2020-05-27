package ru.radiationx.data.api.service

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.entity.common.ApiBaseResponse
import ru.radiationx.data.api.entity.config.ConfigResponse

interface ConfigurationService {

    fun get(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<ConfigResponse>>
}