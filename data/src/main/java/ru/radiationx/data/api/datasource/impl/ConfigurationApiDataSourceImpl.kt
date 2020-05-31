package ru.radiationx.data.api.datasource.impl

import io.reactivex.Single
import anilibria.tv.domain.entity.config.ApiAddress
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.ConfigConverter
import ru.radiationx.data.api.datasource.ConfigurationApiDataSource
import ru.radiationx.data.api.service.ConfigurationService
import toothpick.InjectConstructor

@InjectConstructor
class ConfigurationApiDataSourceImpl(
    private val configurationService: ConfigurationService,
    private val configConverter: ConfigConverter
) : ConfigurationApiDataSource {

    override fun get(): Single<List<ApiAddress>> = configurationService
        .get(mapOf("query" to "config"))
        .handleApiResponse()
        .map {
            it.addresses.map { addressResponse ->
                configConverter.toDomain(addressResponse)
            }
        }
}