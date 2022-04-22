package ru.radiationx.anilibria.di

import kotlinx.coroutines.runBlocking
import toothpick.InjectConstructor
import tv.anilibria.core.types.BaseUrl
import tv.anilibria.core.types.asBaseUrl
import tv.anilibria.feature.content.data.BaseUrlsProvider
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
}