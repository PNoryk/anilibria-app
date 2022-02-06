package tv.anilibria.module.domain.remote

import io.reactivex.Single
import tv.anilibria.module.domain.entity.address.ApiAddress

interface ConfigurationRemoteDataSource {
    fun checkAvailable(apiUrl: String): Single<Boolean>
    fun checkApiAvailable(apiUrl: String): Single<Boolean>
    fun getConfiguration(): Single<List<ApiAddress>>
}