package ru.radiationx.data.api.datasource

import io.reactivex.Single
import ru.radiationx.data.adomain.entity.config.ApiAddress

interface ConfigurationApiDataSource {
    fun get(): Single<List<ApiAddress>>
}