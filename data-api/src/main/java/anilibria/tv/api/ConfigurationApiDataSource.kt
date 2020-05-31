package anilibria.tv.api

import io.reactivex.Single
import anilibria.tv.domain.entity.config.ApiAddress

interface ConfigurationApiDataSource {
    fun get(): Single<List<ApiAddress>>
}