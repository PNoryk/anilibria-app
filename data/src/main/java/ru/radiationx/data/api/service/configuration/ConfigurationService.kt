package ru.radiationx.data.api.service.configuration

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.remote.common.ApiBaseResponse
import ru.radiationx.data.api.remote.common.handleApiResponse
import ru.radiationx.data.api.remote.config.ConfigResponse

class ConfigurationService(
    private val configurationApi: ConfigurationApi
) {

    fun get(): Single<ConfigResponse> = configurationApi
        .get(mapOf("query" to "config"))
        .handleApiResponse()
}