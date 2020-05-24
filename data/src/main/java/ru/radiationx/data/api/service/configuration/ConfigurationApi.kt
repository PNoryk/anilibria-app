package ru.radiationx.data.api.service.configuration

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.remote.common.ApiBaseResponse
import ru.radiationx.data.api.remote.config.ConfigResponse

interface ConfigurationApi {

    fun get(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<ConfigResponse>>
}