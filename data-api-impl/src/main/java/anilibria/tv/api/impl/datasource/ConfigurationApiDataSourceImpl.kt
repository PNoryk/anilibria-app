package anilibria.tv.api.impl.datasource

import io.reactivex.Single
import anilibria.tv.domain.entity.config.ApiAddress
import anilibria.tv.api.impl.common.handleApiResponse
import anilibria.tv.api.impl.converter.ConfigConverter
import anilibria.tv.api.ConfigurationApiDataSource
import anilibria.tv.api.impl.service.ConfigurationService
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