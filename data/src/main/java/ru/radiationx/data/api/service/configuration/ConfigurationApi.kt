package ru.radiationx.data.api.service.configuration

import io.reactivex.Single
import ru.radiationx.data.api.remote.config.ConfigResponse

interface ConfigurationApi {

    fun get(): Single<ConfigResponse>
}