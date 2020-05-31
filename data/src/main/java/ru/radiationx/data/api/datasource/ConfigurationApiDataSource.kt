package ru.radiationx.data.api.datasource

import io.reactivex.Single
import anilibria.tv.domain.entity.config.ApiAddress

interface ConfigurationApiDataSource {
    fun get(): Single<List<ApiAddress>>
}