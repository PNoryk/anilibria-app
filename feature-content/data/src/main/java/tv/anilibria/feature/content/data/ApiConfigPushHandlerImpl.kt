package tv.anilibria.feature.content.data

import com.squareup.moshi.Moshi
import toothpick.InjectConstructor
import tv.anilibria.feature.networkconfig.data.address.ApiConfigController
import tv.anilibria.feature.networkconfig.data.response.ApiConfigResponse
import tv.anilibria.feature.networkconfig.data.toDomain

@InjectConstructor
class ApiConfigPushHandlerImpl(
    private val apiConfigController: ApiConfigController,
    private val moshi: Moshi
) : ApiConfigPushHandler {

    override suspend fun handlePushData(rawData: String) {
        runCatching {
            val adapter = moshi.adapter(ApiConfigResponse::class.java)
            val config = adapter.fromJson(rawData)!!.toDomain()
            apiConfigController.setAddresses(config.addresses)
        }.onFailure {
            it.printStackTrace()
        }
    }
}