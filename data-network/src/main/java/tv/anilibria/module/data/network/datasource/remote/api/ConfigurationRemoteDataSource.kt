package tv.anilibria.module.data.network.datasource.remote.api

import io.reactivex.Single
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.domain.entity.address.ApiAddress

interface ConfigurationRemoteDataSource {
    fun checkAvailable(apiUrl: String): Single<Boolean>
    fun checkApiAvailable(apiUrl: String): Single<Boolean>
    fun getConfiguration(): Single<List<ApiAddress>>
    fun check(client: IClient, apiUrl: String): Single<Boolean>
    fun getMergeConfig(): Single<List<ApiAddress>>
    fun getConfigFromApi(): Single<List<ApiAddress>>
    fun getConfigFromReserve(): Single<List<ApiAddress>>
    fun getReserve(url: String): Single<List<ApiAddress>>
}