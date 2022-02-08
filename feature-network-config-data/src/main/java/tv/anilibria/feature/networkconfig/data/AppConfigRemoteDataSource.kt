package tv.anilibria.feature.networkconfig.data

import io.reactivex.Single
import tv.anilibria.feature.networkconfig.data.response.ApiConfigResponse

interface AppConfigRemoteDataSource {

    fun checkAvailable(apiUrl: String): Single<Boolean>
    fun checkApiAvailable(apiUrl: String): Single<Boolean>
    fun getConfiguration(): Single<ApiConfigResponse>
}