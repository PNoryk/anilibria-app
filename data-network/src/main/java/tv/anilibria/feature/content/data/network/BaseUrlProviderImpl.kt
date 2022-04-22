package tv.anilibria.feature.content.data.network

import kotlinx.coroutines.runBlocking
import toothpick.InjectConstructor
import tv.anilibria.core.types.BaseUrl
import tv.anilibria.core.types.asBaseUrl
import tv.anilibria.plugin.data.network.BaseUrlsProvider
import tv.anilibria.feature.networkconfig.data.address.ApiConfigController

@InjectConstructor
class BaseUrlProviderImpl(
    private val apiConfigController: ApiConfigController
) : BaseUrlsProvider {

    override val widgetsSite: BaseUrl
        get() = runBlocking { apiConfigController.getActive().widgetsSite.asBaseUrl() }

    override val site: BaseUrl
        get() = runBlocking { apiConfigController.getActive().widgetsSite.asBaseUrl() }

    override val baseImages: BaseUrl
        get() = runBlocking { apiConfigController.getActive().widgetsSite.asBaseUrl() }

    override val base: BaseUrl
        get() = runBlocking { apiConfigController.getActive().widgetsSite.asBaseUrl() }

    override val api: BaseUrl
        get() = runBlocking { apiConfigController.getActive().api.asBaseUrl() }
}