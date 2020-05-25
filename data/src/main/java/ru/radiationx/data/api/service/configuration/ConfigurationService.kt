package ru.radiationx.data.api.service.configuration

import io.reactivex.Single
import ru.radiationx.data.adomain.config.ApiAddress
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.ConfigConverter
import ru.radiationx.data.api.remote.config.ConfigResponse
import toothpick.InjectConstructor

@InjectConstructor
class ConfigurationService(
    private val configurationApi: ConfigurationApi,
    private val configConverter: ConfigConverter
) {

    fun get(): Single<List<ApiAddress>> = configurationApi
        .get(mapOf("query" to "config"))
        .handleApiResponse()
        .map {
            it.addresses.map { addressResponse ->
                configConverter.toDomain(addressResponse)
            }
        }
}