package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.config.ApiAddress
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.ConfigConverter
import ru.radiationx.data.api.service.ConfigurationService
import toothpick.InjectConstructor

@InjectConstructor
class ConfigurationDataSource(
    private val configurationService: ConfigurationService,
    private val configConverter: ConfigConverter
) {

    fun get(): Single<List<ApiAddress>> = configurationService
        .get(mapOf("query" to "config"))
        .handleApiResponse()
        .map {
            it.addresses.map { addressResponse ->
                configConverter.toDomain(addressResponse)
            }
        }
}