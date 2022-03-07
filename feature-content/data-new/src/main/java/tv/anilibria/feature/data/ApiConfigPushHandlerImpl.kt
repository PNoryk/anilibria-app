package tv.anilibria.feature.data

import com.squareup.moshi.Moshi
import tv.anilibria.feature.networkconfig.data.address.ApiConfigController
import tv.anilibria.feature.networkconfig.data.response.ApiConfigResponse
import tv.anilibria.feature.networkconfig.data.toDomain

class ApiConfigPushHandlerImpl(
    private val apiConfigController: ApiConfigController,
    private val moshi: Moshi
) : ApiConfigPushHandler {

    override suspend fun handlePushData(rawData: String) {
        runCatching {
            val adapter = moshi.adapter(ApiConfigResponse::class.java)
            val config = adapter.fromJson(rawData)!!.toDomain()
            apiConfigController.setAddresses(config.addresses)
        }
    }
}